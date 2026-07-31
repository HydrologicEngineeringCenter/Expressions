package usace.hec.expressions;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DisplayNodeRegistry implements DisplayNodeProvider{
    private List<DisplayNode> displayNodes;
    private static final String PACKAGE_NAME = "usace.hec.expressions";
    private static final String PACKAGE_PATH = PACKAGE_NAME.replace('.', '/');
    public DisplayNodeRegistry(){
        generateDisplayNodesList();
    }
    @Override
    public  List<DisplayNode> getDisplayNodes() {
        return displayNodes;
    }


    @Override
    public List<DisplayNode> getDisplayNodesByCategory(String category) {
        if (category == null || displayNodes == null) {
            return Collections.emptyList();
        }
        return displayNodes.stream()
                .filter(node -> category.equals(node.category()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DisplayNode> getDisplayNodesByOutputType(ExpressionType type) {
        if (type == null || displayNodes == null) {
            return Collections.emptyList();
        }
        return displayNodes.stream()
                .filter(node -> node.getExpressionResultTypes().contains(type))
                .collect(Collectors.toList());
    }

    private void generateDisplayNodesList() {
        // Accumulate result types per operator
        Map<ExpressionOperator, Set<ExpressionType>> operatorTypes = new EnumMap<>(ExpressionOperator.class);
        
        // 1. Scan package for concrete ExpressionNode implementations
        List<Class<?>> nodeClasses = scanPackageForNodes();

        for (Class<?> clazz : nodeClasses) {
            try {
                // 2. Determine Operator using the static method (no instantiation)
                ExpressionOperator op = (ExpressionOperator) clazz.getMethod("StaticOperator").invoke(null);
                
                if (op != null) {
                    // 3. Determine Result Type by checking implemented interfaces
                    ExpressionType type = getResultTypeFromInterfaces(clazz);
                    
                    if (type != null) {
                        operatorTypes.computeIfAbsent(op, k -> new HashSet<>()).add(type);
                    }
                }
            } catch (Exception e) {
                // Skip classes where reflection fails or StaticOperator is missing
                System.out.println(e);
            }
        }

        // 4. Build DisplayNodes for all operators
        List<DisplayNode> nodes = new ArrayList<>();
        for (ExpressionOperator op : ExpressionOperator.values()) {
            Set<ExpressionType> types = operatorTypes.getOrDefault(op, Collections.emptySet());
            nodes.add(wrap(op, types));
        }
        this.displayNodes = Collections.unmodifiableList(nodes);
    }

    private ExpressionType getResultTypeFromInterfaces(Class<?> clazz) {
        if (BooleanExpressionNode.class.isAssignableFrom(clazz)) {
            return ExpressionType.BOOLEAN;
        }
        if (DateTimeExpressionNode.class.isAssignableFrom(clazz)) {
            return ExpressionType.DATE;
        }
        if (DoubleExpressionNode.class.isAssignableFrom(clazz)) {
            return ExpressionType.DOUBLE;
        }
        if (IntegerExpressionNode.class.isAssignableFrom(clazz)) {
            return ExpressionType.INTEGER;
        }
        if (StringExpressionNode.class.isAssignableFrom(clazz)) {
            return ExpressionType.STRING;
        }
        return null;
    }

    private List<Class<?>> scanPackageForNodes() {
        List<Class<?>> classes = new ArrayList<>();
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(PACKAGE_PATH);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String filePath = URLDecoder.decode(resource.getFile(), StandardCharsets.UTF_8);
                File directory = new File(filePath);

                if (directory.exists() && directory.isDirectory()) {
                    // Recursively scan the root package and all sub-packages
                    scanDirectoryRecursively(directory, PACKAGE_NAME, classes);
                }
            }
        } catch (Exception e) {
            // In production, consider logging this or falling back to ServiceLoader
        }
        return classes;
    }

    private void scanDirectoryRecursively(File directory, String packageName, List<Class<?>> classes) {
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                // Recurse into subdirectories, appending to the package name
                String subPackageName = packageName + "." + file.getName();
                scanDirectoryRecursively(file, subPackageName, classes);
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().replace(".class", "");
                try {
                    Class<?> clazz = Class.forName(className);
                    // Only consider concrete classes that implement ExpressionNode
                    if (ExpressionNode.class.isAssignableFrom(clazz)
                            && !clazz.isInterface()
                            && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                        classes.add(clazz);
                    }
                } catch (ClassNotFoundException ignored) {
                }
            }
        }
    }

    private DisplayNode wrap(ExpressionOperator op, Set<ExpressionType> types) {
        List<ExpressionType> immutableTypes = Collections.unmodifiableList(new ArrayList<>(types));
        return new DisplayNode() {
            @Override 
            public String displayName(boolean infix) { 
                return infix ? op.getInfixName() : op.getPrefixName(); 
            }
            @Override 
            public String category() { 
                return op.getCategory(); 
            }
            @Override 
            public String defaultSyntax(boolean infix) { 
                return infix ? op.getInfixSyntax() : op.getPrefixSyntax(); 
            }
            @Override 
            public List<ExpressionType> getExpressionResultTypes() {
                return immutableTypes;
            }
        };
    }
}
