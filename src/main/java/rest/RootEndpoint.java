package rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Path("")
public class RootEndpoint {
    @GET
    public String help() {
        StringBuilder builder = new StringBuilder();

        List<Class> classes = new ArrayList<>();
        classes.add(LACandidateCheckEndpoint.class);
        classes.add(K2DataSourceEndpoint.class);
        classes.add(UserEndpoint.class);
        classes.add(K2LACandidateMatchingResultEndpoint.class);
        classes.add(LADataPointEndpoint.class);
        classes.add(LADipEndpoint.class);
        classes.add(LAEvaluationEndpoint.class);

        for (Class c : classes) {
            Annotation[] annotations = c.getAnnotations();
            for (Annotation a : annotations) {
                if (a instanceof Path) {
                    String value = ((Path) a).value();

                    String line = "<h3><a href=\"" + "/K2DataProvider" + value + "\">" + value + "</a></h3>";

                    builder.append(line);
                }
            }
        }

        return builder.toString();
    }

    public static String getResourceInformation(Class<?> c) {
        StringBuilder builder = new StringBuilder();

        String path = c.getAnnotation(Path.class).value();

        builder.append("<h2><a href=\"" + "/K2DataProvider" + "\">" + "UP" + "</a></h2>");

        for (Method method : c.getMethods()) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation a : annotations) {
                if (a instanceof Path) {
                    String value = ((Path) a).value();

                    String line = "<h3><a href=\"" + "/K2DataProvider" + path + value.replace("{nr}", "0") + "\">" + value + "</a></h3>";

                    builder.append(line);
                }
            }
        }

        return builder.toString();
    }
}
