package de.muenchen.isi.configuration.search;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.hibernate.search.backend.elasticsearch.client.ElasticsearchHttpClientConfigurationContext;
import org.hibernate.search.backend.elasticsearch.client.ElasticsearchHttpClientConfigurer;

public class ElasticsearchKeepAliveConfig implements ElasticsearchHttpClientConfigurer {

    public static final int MILLISECOND = 1;
    public static final int SECOND = 1000 * MILLISECOND;

    @Override
    public void configure(ElasticsearchHttpClientConfigurationContext context) {
        var clientBuilder = context.clientBuilder();
        clientBuilder.setKeepAliveStrategy((httpResponse, httpContext) -> {
            HeaderElementIterator it = new BasicHeaderElementIterator(
                httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE)
            );

            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                String param = he.getName();
                String value = he.getValue();
                if (value != null && param.equalsIgnoreCase("timeout")) {
                    try {
                        var timeoutSeconds = Long.parseLong(value);
                        return timeoutSeconds * SECOND;
                    } catch (NumberFormatException ignore) {}
                }
            }

            // Connections nicht undendlich lange offen halten,
            // da Netzwerk-Firewall sie sonst evtl. mit deny auslaufen lässt.
            return 30 * SECOND;
        });
    }
}
