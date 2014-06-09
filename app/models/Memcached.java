package models;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;
import play.Play;

import java.io.IOException;

/**
 * Created by shimizu on 2014/06/09.
 */
public class Memcached {
	private static String MEMCACHE_HOST = Play.application().configuration().getString("memcached.host");
	private static String MEMCACHE_USER = Play.application().configuration().getString("memcached.user");
	private static String MEMCACHE_PASSWORD = Play.application().configuration().getString("memcached.password");

	private static AuthDescriptor ad = new AuthDescriptor(new String[] { "PLAIN" },
			new PlainCallbackHandler(MEMCACHE_USER, MEMCACHE_PASSWORD));
	private static MemcachedClient mc;

	static {
		try {
		mc = new MemcachedClient(
				new ConnectionFactoryBuilder()
						.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY)
						.setAuthDescriptor(ad).build(),
				AddrUtil.getAddresses(MEMCACHE_HOST));
		} catch (IOException ioe) {
			System.err.println("Couldn't create a connection to MemCachier: \nIOException "
					+ ioe.getMessage());
		}
	}

	public static void set(String key, Object value){
		mc.set(key, 0, value);
	}

	public static Object get(String key){
		return mc.get(key);
	}
}
