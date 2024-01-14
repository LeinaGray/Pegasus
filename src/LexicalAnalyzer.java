import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    private static final Map<Pattern, String> tokenMap = Tokens.getTokenMap(); // Access token map from separate file

    static {
        // Add your regex patterns and token names here
        tokenMap.put(Pattern.compile("\\b(int|float|char|bool)\\b"), "DATA_TYPE");
        tokenMap.put(Pattern.compile("[a-zA-Z]+"), "IDENTIFIER");
        tokenMap.put(Pattern.compile("\\d+"), "INTEGER_LITERAL");
        tokenMap.put(Pattern.compile("[=+\\-*/;]"), "OPERATOR");
        // ... Add more patterns as needed
    }

    public static List<String[]> generateSymbolTable(String code) {
        List<String[]> symbolTable = new ArrayList<>();
        StringBuilder currentLexeme = new StringBuilder();

        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);

            // Check for matching patterns
            for (Pattern pattern : tokenMap.keySet()) {
                Matcher matcher = pattern.matcher(code.substring(i));
                if (matcher.lookingAt()) {
                    String lexeme = matcher.group();
                    String tokenName = tokenMap.get(pattern);
                    symbolTable.add(new String[]{lexeme, tokenName});
                    i += lexeme.length() - 1; // Adjust index for next iteration
                    currentLexeme = new StringBuilder(); // Reset for next lexeme
                    break;
                }
            }

            // Handle single-character lexemes
            if (currentLexeme.length() == 0 && !Character.isWhitespace(c)) {
                String lexeme = String.valueOf(c);
                // String tokenName = getClosestTokenName(lexeme); // Check for potential misspellings
                // symbolTable.add(new String[]{lexeme, tokenName});
            } else {
                currentLexeme.append(c);
            }
        }

        // Add any remaining lexeme at the end
        if (currentLexeme.length() > 0) {
            String lexeme = currentLexeme.toString();
            // String tokenName = getClosestTokenName(lexeme);
            // symbolTable.add(new String[]{lexeme, tokenName});
        }

        return symbolTable;
    }

    // private static String getClosestTokenName(String lexeme) {
    //     // Implement your Levenshtein distance calculation here to find the closest match
    //     // For simplicity, return a default token name for now
    //     return "UNKNOWN";
    // }

    public static void main(String[] args) {
        String code = "int num=1+1; asdfasdf";
        List<String[]> symbolTable = generateSymbolTable(code);
        for (String[] entry : symbolTable) {
            System.out.println(entry[0] + "\t\t" + entry[1]);
        }
    }
}