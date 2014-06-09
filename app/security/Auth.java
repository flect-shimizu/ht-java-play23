package security;

import models.CacheService;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.util.UUID;

/**
 * Created by shimizu on 2014/06/09.
 */
public class Auth extends Security.Authenticator {

	public static final String UUID_KEY = "uuid";

	public static void initUUID(Http.Session session) {
		String uuid = UUID.randomUUID().toString();
		CacheService.set(uuid, "session");
		session.put(UUID_KEY, uuid);
	}

	public static void clearUUID(Http.Session session) {
		String uuid = session.get(UUID_KEY);
		if (uuid != null) {
			CacheService.remove(uuid);
			session.remove(UUID_KEY);
		}
	}

	@Override
	public String getUsername(Http.Context context) {
		String uuid = context.session().get(UUID_KEY);
		Logger.info("Cookie UUID : " + uuid);
		Object session = CacheService.get(uuid);
		return (session == null) ? null : String.valueOf(session);
	}

	@Override
	public Result onUnauthorized(Http.Context arg0) {
		return redirect(controllers.routes.Account.login());
	}
}
