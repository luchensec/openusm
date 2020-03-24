package com.openusm.collector.protocol;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.security.PrivAES128;
import org.snmp4j.security.PrivAES192;
import org.snmp4j.security.PrivAES256;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

/**
 * SNMP工具
 */
public class SnmpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnmpUtils.class);

    private static final Set<OID> usmErr;

    static {
        usmErr = new HashSet<>(
                Arrays.asList(SnmpConstants.usmStatsDecryptionErrors, SnmpConstants.usmStatsNotInTimeWindows,
                        SnmpConstants.usmStatsUnknownEngineIDs, SnmpConstants.usmStatsUnknownUserNames,
                        SnmpConstants.usmStatsUnsupportedSecLevels, SnmpConstants.usmStatsWrongDigests));
    }

    private int version;
    private Address address;
    private String community;
    private String user;
    private Auth auth;
    private String authPass;
    private Priv priv;
    private String privPass;

    /**
     * SNMP V2
     * 
     * @param host 地址（udp:192.168.1.1:/161）
     * @param community 团体字
     */
    public SnmpUtils(String host, String community) {
        this.version = SnmpConstants.version2c;
        this.address = GenericAddress.parse(host);
        this.community = community;
    }

    /**
     * SNMP V3
     * 
     * @param host 地址（udp:192.168.1.1:/161）
     * @param user 用户名
     * @param auth 认证协议（MD5/SHA）
     * @param authPass 认证密码
     * @param priv 加密协议（DES/AES）
     * @param privPass 加密密钥
     */
    public SnmpUtils(String host, String user, Auth auth, String authPass, Priv priv, String privPass) {
        this.version = SnmpConstants.version3;
        this.address = GenericAddress.parse(host);
        this.user = user;
        this.auth = auth;
        this.authPass = authPass;
        this.priv = priv;
        this.privPass = privPass;
    }

    private Snmp createSnmp() throws IOException {
        Snmp snmp = new Snmp(createTransportMapping());

        if (version == SnmpConstants.version3) {
            addUsmUser(snmp);
        }
        snmp.listen();
        return snmp;
    }

    private TransportMapping createTransportMapping() throws IOException {
        TransportMapping transport = null;

        if (address instanceof TcpAddress) {
            transport = new DefaultTcpTransportMapping();
        } else if (address instanceof UdpAddress) {
            transport = new DefaultUdpTransportMapping();
        }

        return transport;
    }

    private void addUsmUser(Snmp snmp) {
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);

        OctetString securityName = new OctetString(user);

        OctetString authPwd = null;
        if (!auth.none()) {
            authPwd = new OctetString(authPass);
        }

        OctetString privPwd = null;
        if (!priv.none()) {
            privPwd = new OctetString(privPass);
        }

        UsmUser usmUser = new UsmUser(securityName, auth.toOID(), authPwd, priv.toOID(), privPwd);
        snmp.getUSM().addUser(securityName, usmUser);
    }

    private Target createTarget() {
        Target target = null;

        if (version == SnmpConstants.version3) {
            UserTarget userTarget = new UserTarget();
            if (auth.none()) {
                userTarget.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SNMP认证级别：NOAUTH_NOPRIV");
                }
            } else {
                if (priv.none()) {
                    userTarget.setSecurityLevel(SecurityLevel.AUTH_NOPRIV);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SNMP认证级别：AUTH_NOPRIV");
                    }
                } else {
                    userTarget.setSecurityLevel(SecurityLevel.AUTH_PRIV);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SNMP认证级别：AUTH_PRIV");
                    }
                }
            }
            userTarget.setSecurityName(new OctetString(user));

            target = userTarget;
        } else {
            CommunityTarget communityTarget = new CommunityTarget();
            communityTarget.setCommunity(new OctetString(community));

            target = communityTarget;
        }

        target.setAddress(address);
        target.setVersion(version);
        target.setRetries(0);
        target.setTimeout(2000);

        return target;
    }

    private PDU createPDU() {
        PDU pdu = null;

        if (version == SnmpConstants.version3) {
            pdu = new ScopedPDU();
        } else {
            pdu = new PDU();
        }

        return pdu;
    }

    /**
     * 测试能否连通
     * 
     * @return
     * @throws IOException
     */
    public boolean test() throws IOException {
        Snmp snmp = createSnmp();
        Target target = createTarget();
        PDU pdu = createPDU();

        ResponseEvent responseEvent = snmp.get(pdu, target);
        PDU response = responseEvent.getResponse();

        try {
            if (response == null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SNMP连接超时");
                }
                return false;
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SNMP响应状态: [{}:{}]", response.getErrorStatus(), response.getErrorStatusText());
            }

            if (response.getErrorStatus() != PDU.noError) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SNMP响应出错");
                }
                return false;
            }

            Vector<?> variableBindings = response.getVariableBindings();
            for (Object object : variableBindings) {
                VariableBinding variableBinding = (VariableBinding) object;

                if (usmErr.contains(variableBinding.getOid())) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("SNMP验证失败");
                    }
                    return false;
                }
            }
        } finally {
            try {
                snmp.close();
            } catch (Exception e) {
                // nothing to do...
            }
        }

        return true;
    }

    private List<String> readResponse(ResponseEvent responseEvent) {
        List<String> result = new ArrayList<>();

        PDU response = responseEvent.getResponse();
        if (response == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SNMP连接超时");
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("SNMP响应状态: [{}:{}]", response.getErrorStatus(), response.getErrorStatusText());
                }
            }

            Vector<?> variableBindings = response.getVariableBindings();
            for (Object object : variableBindings) {
                VariableBinding variableBinding = (VariableBinding) object;

                if (usmErr.contains(variableBinding.getOid())) {
                    throw new RuntimeException("SNMP验证失败");
                } else {
                    result.add(variableToString(variableBinding));
                }
            }
        }

        return result;
    }

    private List<String> readTableResponse(TableEvent tableEvent) {
        List<String> result = new ArrayList<>();

        if (tableEvent.getStatus() == TableEvent.STATUS_OK) {
            VariableBinding[] variableBindings = tableEvent.getColumns();

            for (VariableBinding variableBinding : variableBindings) {
                result.add(variableToString(variableBinding));
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("TableEvent返回状态:{},ErrorMessage:{}", tableEvent.getStatus(), tableEvent.getErrorMessage());
            }
        }

        return result;
    }

    private static String variableToString(VariableBinding binding) {
        if (binding == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("此OID没有对应的列");
            }
            return null;
        }

        Variable variable = binding.getVariable();

        // noSuch?
        if (variable.isException()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("此OID[{}]对应的值[{}]不是有效的", binding.getOid().toString(), variable.toString());
            }
            return null;
        }

        if (variable instanceof OctetString) {
            // 中文乱码显示成'9E:07:20:52'，但MAC本来就是这样
            if (!binding.getOid().toString().startsWith("1.3.6.1.2.1.2.2.1.6")) {
                OctetString o = (OctetString) variable;
                return new String(o.getValue(), Charset.forName("GB2312")).trim();
            }
        } else if (variable instanceof TimeTicks) {
            TimeTicks t = (TimeTicks) variable;
            return String.valueOf(t.toMilliseconds()); // 毫秒
        }

        return variable.toString();
    }

    /**
     * 同步发送SNMP GET请求，获取OID对应数据
     * 
     * @param oid 单个OID
     * @return
     * @throws IOException
     */
    public String get(String oid) throws IOException {
        List<String> result = get(Arrays.asList(oid));
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    /**
     * 同步发送SNMP GET请求，获取OID对应数据
     * 
     * @param oids 多个OID集合
     * @return
     * @throws IOException
     */
    public List<String> get(List<String> oids) throws IOException {
        Snmp snmp = createSnmp();
        Target target = createTarget();
        PDU pdu = createPDU();

        for (String oid : oids) {
            pdu.add(new VariableBinding(new OID(oid)));
        }

        ResponseEvent responseEvent = snmp.get(pdu, target);
        List<String> result = readResponse(responseEvent);

        try {
            snmp.close();
        } catch (Exception e) {
            // nothing to do...
        }

        return result;
    }

    /**
     * 异步发送SNMP GET请求，获取OID对应数据
     * 
     * @param oid 单个OID
     * @param listener 监听响应的回调
     * @throws IOException
     */
    public void getAsync(String oid, ResponseListener listener) throws IOException {
        getAsync(Arrays.asList(oid), result -> {
            if (listener != null) {
                if (result.isEmpty()) {
                    listener.onResponse(null);
                } else {
                    listener.onResponse(result.get(0));
                }
            }
        });
    }

    /**
     * 异步发送SNMP GET请求，获取OID对应数据
     * 
     * @param oids 多个OID集合
     * @param listener 监听响应的回调
     * @throws IOException
     */
    public void getAsync(List<String> oids, MultiResponseListener listener) throws IOException {
        Snmp snmp = createSnmp();
        Target target = createTarget();
        PDU pdu = createPDU();

        for (String oid : oids) {
            pdu.add(new VariableBinding(new OID(oid)));
        }

        snmp.get(pdu, target, null, responseEvent -> {
            if (responseEvent.getError() != null) {
                return;
            }

            if (listener != null) {
                listener.onResponse(readResponse(responseEvent));
            }

            try {
                snmp.close();
            } catch (Exception e) {
                // nothing to do...
            }
        });
    }

    /**
     * 同步发送SNMP WALK请求，获取表格
     * 
     * @param oids 多个OID集合，对应表格的列
     * @return
     * @throws IOException
     */
    public List<List<String>> getTable(List<String> oids) throws IOException {
        Snmp snmp = createSnmp();
        Target target = createTarget();

        TableUtils utils = new TableUtils(snmp, new DefaultPDUFactory(PDU.GETBULK));

        OID[] columns = oids.stream().map(oid -> {
            return new OID(oid);
        }).collect(Collectors.toList()).toArray(new OID[] {});

        List<List<String>> result = new ArrayList<>();

        List<?> table = utils.getTable(target, columns, null, null);
        for (Object object : table) {
            TableEvent tableEvent = (TableEvent) object;

            result.add(readTableResponse(tableEvent));
        }

        try {
            snmp.close();
        } catch (Exception e) {
            // nothing to do...
        }

        return result;
    }

    /**
     * 异步发送SNMP WALK请求，获取表格
     * 
     * @param oids 多个OID集合，对应表格的列
     * @param listener 监听响应的回调
     * @throws IOException
     */
    public void getTableAsync(List<String> oids, TableListener listener) throws IOException {
        Snmp snmp = createSnmp();
        Target target = createTarget();

        TableUtils utils = new TableUtils(snmp, new DefaultPDUFactory(PDU.GETBULK));

        OID[] columns = oids.stream().map(oid -> {
            return new OID(oid);
        }).collect(Collectors.toList()).toArray(new OID[] {});

//        utils.getTable(target, columns, new org.snmp4j.util.TableListener() {
//            private final List<List<String>> list = new ArrayList<>();
//
//            @Override
//            public boolean next(TableEvent tableEvent) {
//                list.add(readTableResponse(tableEvent));
//                return true;
//            }
//
//            @Override
//            public void finished(TableEvent arg0) {
//                if (listener != null) {
//                    listener.onResponse(list);
//                }
//
//                try {
//                    snmp.close();
//                } catch (Exception e) {
//                    // nothing to do...
//                }
//            }
//        }, null, null, null);
    }

    public static void main(String[] args) {
        SnmpUtils utils = new SnmpUtils(String.format("udp:%s/%s", "192.168.10.126", "161"), "snmpv3user",
                Auth.MD5, "1qaz2wsx", Priv.NONE, null);
        try {
            System.out.println(utils.get("1.3.6.1.2.1.1.5.0")); // 主机名
            System.out.println(utils.get("1.3.6.1.1.1.1")); // 瞎写的，测试
            System.out.println(utils.get("1.3.6.1.2.1.25.2.2.0")); // 内存大小

            System.out.println(utils.get(Arrays.asList("1.3.6.1.2.1.1.5.0", "1.3.6.1.1.1.1", "1.3.6.1.2.1.25.2.2.0")));

            System.out.println("-----");

            utils.getAsync("1.3.6.1.2.1.1.5.0", result -> {
                System.out.println(result);
            });
            utils.getAsync("1.3.6.1.1.1.1", result -> {
                System.out.println(result);
            });
            utils.getAsync("1.3.6.1.2.1.25.2.2.0", result -> {
                System.out.println(result);
            });

            utils.getAsync(Arrays.asList("1.3.6.1.2.1.1.5.0", "1.3.6.1.1.1.1", "1.3.6.1.2.1.25.2.2.0"), result -> {
                System.out.println(result);
            });

            System.out.println("-----");

            List<List<String>> table = utils.getTable(Arrays.asList( //
                    "1.3.6.1.1.1.1", // 瞎写的，测试
                    "1.3.6.1.2.1.2.2.1.2", // 网络接口描述
                    "1.3.6.1.2.1.2.2.1.3", // 网络接口类型
                    "1.3.6.1.2.1.2.2.1.4", // 发送和接收的最大IP数据报
                    "1.3.6.1.2.1.2.2.1.5", // 接口带宽bps
                    "1.3.6.1.2.1.2.2.1.6", // 接口物理地址
                    "1.3.6.1.2.1.2.2.1.8", // 接口操作状态
                    "1.3.6.1.2.1.2.2.1.10", // 接收字节数
                    "1.3.6.1.2.1.2.2.1.16", // 发送字节数
                    "1.3.6.1.2.1.2.2.1.11", // 接收数据包个数
                    "1.3.6.1.2.1.2.2.1.17" // 发送数据包个数
            ));
            for (List<String> list : table) {
                System.out.println(list);
            }

            System.out.println("-----");

            utils.getTableAsync(Arrays.asList( //
                    "1.3.6.1.1.1.1", // 瞎写的，测试
                    "1.3.6.1.2.1.2.2.1.2", // 网络接口描述
                    "1.3.6.1.2.1.2.2.1.3", // 网络接口类型
                    "1.3.6.1.2.1.2.2.1.4", // 发送和接收的最大IP数据报
                    "1.3.6.1.2.1.2.2.1.5", // 接口带宽bps
                    "1.3.6.1.2.1.2.2.1.6", // 接口物理地址
                    "1.3.6.1.2.1.2.2.1.8", // 接口操作状态
                    "1.3.6.1.2.1.2.2.1.10", // 接收字节数
                    "1.3.6.1.2.1.2.2.1.16", // 发送字节数
                    "1.3.6.1.2.1.2.2.1.11", // 接收数据包个数
                    "1.3.6.1.2.1.2.2.1.17" // 发送数据包个数
            ), result -> {
                for (List<String> row : result) {
                    System.out.println(row);
                }
            });

            System.out.println("-----");

            Thread.sleep(3000);
        } catch (Exception e) {
            LOGGER.error("SNMP测试出错", e);
        }
    }

    @FunctionalInterface
    public static interface ResponseListener {
        void onResponse(String result);
    }

    @FunctionalInterface
    public static interface MultiResponseListener {
        void onResponse(List<String> result);
    }

    @FunctionalInterface
    public static interface TableListener {
        void onResponse(List<List<String>> list);
    }

    public static enum Auth {
        NONE(0), MD5(1), SHA(2);

        private final int value;

        private Auth(int value) {
            this.value = value;
        }

        public OID toOID() {
            if (value == 2) {
                return AuthSHA.ID;
            } else if (value == 1) {
                return AuthMD5.ID;
            } else {
                return null;
            }
        }

        public boolean none() {
            return value == 0;
        }
    }

    public static enum Priv {
        NONE(0), DES(1), AES128(2), AES192(3), AES256(4);

        private final int value;

        private Priv(int value) {
            this.value = value;
        }

        public OID toOID() {
            if (value == 4) {
                return PrivAES256.ID;
            } else if (value == 3) {
                return PrivAES192.ID;
            } else if (value == 2) {
                return PrivAES128.ID;
            } else if (value == 1) {
                return PrivDES.ID;
            } else {
                return null;
            }
        }

        public boolean none() {
            return value == 0;
        }
    }
}
