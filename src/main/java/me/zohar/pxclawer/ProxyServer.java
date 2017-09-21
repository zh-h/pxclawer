package me.zohar.pxclawer;

import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.littleshoot.proxy.mitm.CertificateSniffingMitmManager;
import org.littleshoot.proxy.mitm.RootCertificateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class ProxyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyServer.class);

    public static void main(String[] args) throws RootCertificateException {
        LOGGER.info("Server Start");
        HttpProxyServer server =
                DefaultHttpProxyServer.bootstrap()
                        .withAddress(new InetSocketAddress("0.0.0.0", 8888))
                        .withManInTheMiddle(new CertificateSniffingMitmManager())
                        .withFiltersSource(new ResourceFilter())
                        .start();
    }
}
