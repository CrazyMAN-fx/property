package controllers;

import category.AdParamsHelper;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Application extends Controller {

    public static F.Promise<Result> proxy() {
        String url = request().queryString().get("url")[0];
        final F.Promise<Result> resultPromise = WS.url(url).get().map(
                new F.Function<WSResponse, Result>() {
                    public Result apply(WSResponse response) {
                        byte[] array = response.asByteArray();
                        return ok(array);
                    }
                }
        );
        return resultPromise;
    }

    public static Result index() {
        return ok(index.render(AdParamsHelper.filterParams));
    }

    public static Result favourite() {
        return ok(favourite.render());
    }

    public static Result createNew() {
        return ok(new_adv.render(AdParamsHelper.filterParams,AdController.generateAdId().toString()));
    }

    public static Result registration() {
        return ok(registration.render());
    }

    public static Result quickRegistration() {
        return ok(quick_registration.render());
    }

    public static Result loginPage() {
        return ok(login.render());
    }

    public static Result office() {
        return ok(office.render());
    }
}
