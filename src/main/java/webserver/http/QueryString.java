package webserver.http;

import javafx.util.Pair;
import utils.StringParseUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueryString {
    private final static String PATH_SEPARATOR = "?";
    private final static String PARAMETER_SEPARATOR = "&";
    private final static String KEY_VALUE_SEPARATOR = "=";

    private Map<String, String> parameterMap;

    private QueryString(Map<String, String> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public static QueryString parse(String url) {
        Map<String, String> parameterMap = getParameterStrings(url).stream()
                .map(value -> StringParseUtils.makeKeyValuePair(value, KEY_VALUE_SEPARATOR))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        return new QueryString(parameterMap);
    }

    private static List<String> getParameterStrings(String url) {
        String queryString = url.substring(url.indexOf(PATH_SEPARATOR) + 1);
        return Stream.of(queryString.split(PARAMETER_SEPARATOR))
                .filter(value -> value.contains(KEY_VALUE_SEPARATOR))
                .collect(Collectors.toList());
    }

    public int parameterSize() {
        return parameterMap.size();
    }

    public String get(String key) {
        return parameterMap.get(key);
    }
}
