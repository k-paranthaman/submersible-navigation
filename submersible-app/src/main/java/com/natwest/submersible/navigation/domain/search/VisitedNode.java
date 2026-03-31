package com.natwest.submersible.navigation.domain.search;

import com.natwest.submersible.navigation.domain.Position;
import com.natwest.submersible.navigation.domain.enums.Direction;

/**
 * VisitedNode is a value object used to track visited probe states during pathfinding algorithms (such as BFS).
 * It encapsulates a position and direction, and provides proper equals and hashCode implementations for use in sets and maps.
 * <p>
 * Usage:
 * <pre>
 *     Set&lt;VisitedNode&gt; visited = new HashSet&lt;&gt;();
 *     visited.add(new VisitedNode(position, direction));
 * </pre>
 */

public record VisitedNode(Position position, Direction direction) {
}
