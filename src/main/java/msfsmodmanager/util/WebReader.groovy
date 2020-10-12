package msfsmodmanager.util

import groovy.transform.CompileStatic

@CompileStatic
class WebReader {
    private static String BROWSER_CLIENT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
    
    public static String readUrl(String url) {
        new URL(url).getText(connectTimeout: 10 * 1000, readTimeout: 10 * 1000,
            useCaches: true, allowUserInteraction: false,
            requestProperties: ['User-Agent': BROWSER_CLIENT_USER_AGENT])
    }
}

