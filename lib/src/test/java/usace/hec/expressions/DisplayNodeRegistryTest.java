package usace.hec.expressions;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class DisplayNodeRegistryTest {

    private DisplayNodeProvider provider;

    @Before
    public void setUp() {
        // Instantiate the registry. It will scan the classpath for ExpressionNode implementations.
        provider = new DisplayNodeRegistry();
    }

    @Test
    public void testGetDisplayNodesReturnsNonEmptyList() {
        List<DisplayNode> nodes = provider.getDisplayNodes();
        assertNotNull("Display nodes list should not be null", nodes);
        assertFalse("Registry should discover at least one ExpressionNode implementation", nodes.isEmpty());
    }

    @Test
    public void testGetDisplayNodesReturnsImmutableList() {
        List<DisplayNode> nodes = provider.getDisplayNodes();
        try {
            nodes.add(null);
            fail("Expected UnsupportedOperationException when modifying the list");
        } catch (UnsupportedOperationException e) {
            // Expected: list is immutable
        }
    }

    @Test
    public void testDisplayNodePropertiesArePopulated() {
        List<DisplayNode> nodes = provider.getDisplayNodes();
        if (nodes.isEmpty()) return;

        DisplayNode node = nodes.get(0);
        assertNotNull("Infix display name", node.displayName(true));
        assertNotNull("Prefix display name", node.displayName(false));
        assertNotNull("Category", node.category());
        assertNotNull("Infix syntax", node.defaultSyntax(true));
        assertNotNull("Prefix syntax", node.defaultSyntax(false));
        assertNotNull("Result types", node.getExpressionResultTypes());
    }

    @Test
    public void testGetDisplayNodesByCategoryFiltersCorrectly() {
        List<DisplayNode> allNodes = provider.getDisplayNodes();
        if (allNodes.isEmpty()) return;

        String knownCategory = allNodes.get(0).category();
        List<DisplayNode> filtered = provider.getDisplayNodesByCategory(knownCategory);
        
        assertNotNull("Filtered list should not be null", filtered);
        assertFalse("Should find nodes for category " + knownCategory, filtered.isEmpty());
        
        for (DisplayNode node : filtered) {
            assertEquals("Node category mismatch", knownCategory, node.category());
        }
    }

    @Test
    public void testGetDisplayNodesByCategoryReturnsEmptyForUnknownOrNull() {
        assertTrue("Unknown category should return empty list", 
                   provider.getDisplayNodesByCategory("NonExistentCategory").isEmpty());
        assertTrue("Null category should return empty list", 
                   provider.getDisplayNodesByCategory(null).isEmpty());
    }

    @Test
    public void testGetDisplayNodesByOutputTypeFiltersCorrectly() {
        List<DisplayNode> allNodes = provider.getDisplayNodes();
        if (allNodes.isEmpty()) return;

        // Find any existing type in the registry to test against
        ExpressionType existingType = null;
        for (DisplayNode node : allNodes) {
            List<ExpressionType> types = node.getExpressionResultTypes();
            if (types != null && !types.isEmpty()) {
                existingType = types.get(0);
                break;
            }
        }
        
        if (existingType == null) return; // No types found in registry

        List<DisplayNode> filtered = provider.getDisplayNodesByOutputType(existingType);
        assertNotNull("Filtered list should not be null", filtered);
        assertFalse("Should find nodes for type " + existingType, filtered.isEmpty());

        for (DisplayNode node : filtered) {
            assertTrue("Node should contain type " + existingType, 
                       node.getExpressionResultTypes().contains(existingType));
        }
    }

    @Test
    public void testGetDisplayNodesByOutputTypeReturnsEmptyForNull() {
        List<DisplayNode> nodes = provider.getDisplayNodesByOutputType(null);
        assertNotNull("Result list should not be null", nodes);
        assertTrue("Null type should return empty list", nodes.isEmpty());
    }
}