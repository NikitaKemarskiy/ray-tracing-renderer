package org.nikita.structure;

import org.nikita.calculation.RayTriangleIntersectionSolver;
import org.nikita.geometry.Ray;
import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

import java.util.*;

public class TriangleBoundingBox {
    private final static int CHILDREN_AMOUNT = 8;
    private final static int AXIS_DIVISION_PARTS = 2;

    private Vector vertex1;
    private Vector vertex2;
    private Set<Triangle> triangles;
    private List<TriangleBoundingBox> children;

    private boolean vertexBelongsTo(Vector vertex) {
        return
            vertex.getX() >= Math.min(vertex1.getX(), vertex2.getX()) &&
            vertex.getX() <= Math.max(vertex1.getX(), vertex2.getX()) &&
            vertex.getY() >= Math.min(vertex1.getY(), vertex2.getY()) &&
            vertex.getY() <= Math.max(vertex1.getY(), vertex2.getY()) &&
            vertex.getZ() >= Math.min(vertex1.getZ(), vertex2.getZ()) &&
            vertex.getZ() <= Math.max(vertex1.getZ(), vertex2.getZ());
    }

    private double getIntersectionDistanceWithRay(Ray ray) {
        Vector origin = ray.getOrigin();
        Vector direction = ray.getDirection();

        double rayXReverse = 1 / direction.getX();
        double rayYReverse = 1 / direction.getY();
        double rayZReverse = 1 / direction.getZ();

        double t1 = (vertex1.getX() - origin.getX()) * rayXReverse;
        double t2 = (vertex2.getX() - origin.getX()) * rayXReverse;
        double t3 = (vertex1.getY() - origin.getY()) * rayYReverse;
        double t4 = (vertex2.getY() - origin.getY()) * rayYReverse;
        double t5 = (vertex1.getZ() - origin.getZ()) * rayZReverse;
        double t6 = (vertex2.getZ() - origin.getZ()) * rayZReverse;

        double tMin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
        double tMax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));

        if (tMax < 0) {
            return -1;
        } else if (tMin > tMax) {
            return -1;
        } else {
            return tMin;
        }
    }

    private List<TriangleBoundingBox> getChildrenIntersectingWithRaySortedByDistance(Ray ray) {
        List<TriangleBoundingBox> childrenIntersectingWithRaySortedByDistance = new LinkedList<>();

        for (TriangleBoundingBox child : children) {
            double intersectionDistance = child.getIntersectionDistanceWithRay(ray);

            if (intersectionDistance == -1) {
                continue;
            }

            childrenIntersectingWithRaySortedByDistance.add(child);
        }

        childrenIntersectingWithRaySortedByDistance.sort(
            Comparator.comparingDouble(child -> child.getIntersectionDistanceWithRay(ray))
        );

        return childrenIntersectingWithRaySortedByDistance;
    }

    private TriangleBoundingBox getChild(Vector vertex) {
        for (TriangleBoundingBox child : children) {
            if (child.vertexBelongsTo(vertex)) {
                return child;
            }
        }

        return null;
    }

    private void initChildren(int depth) {
        children = new ArrayList<>(CHILDREN_AMOUNT);

        if (depth != 0) {
            Vector vertexStep = vertex2
                .subtract(vertex1)
                .divide(
                    new Vector(
                        AXIS_DIVISION_PARTS,
                        AXIS_DIVISION_PARTS,
                        AXIS_DIVISION_PARTS
                    )
                );

            for (double x = vertex1.getX(); x < vertex2.getX(); x += vertexStep.getX()) {
                for (double y = vertex1.getY(); y < vertex2.getY(); y += vertexStep.getY()) {
                    for (double z = vertex1.getZ(); z < vertex2.getZ(); z += vertexStep.getZ()) {
                        Vector vertex1 = new Vector(x, y, z);
                        Vector vertex2 = vertex1.add(vertexStep);
                        children.add(new TriangleBoundingBox(vertex1, vertex2, depth));
                    }
                }
            }
        }
    }

    public TriangleBoundingBox(Vector vertex1, Vector vertex2, int depth) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        triangles = new HashSet<>();
        initChildren(depth - 1);
    }

    public void addTriangle(Triangle triangle) {
        /**
         * Triangle is already in a bounding box
         */
        if (triangles.contains(triangle)) {
            return;
        }

        Iterator<Vector> verticesIterator = triangle.getVertices().iterator();
        boolean triangleBelongsTo = false;

        /**
         * Iterate through all vertices
         */
        while (verticesIterator.hasNext()) {
            Vector vertex = verticesIterator.next();
            boolean vertexBelongsTo = vertexBelongsTo(vertex);
            triangleBelongsTo = triangleBelongsTo || vertexBelongsTo;

            /**
             * Vertex belongs to a bounding box
             */
            if (vertexBelongsTo) {
                /**
                 * Get the child to which a vertex belongs too
                 */
                TriangleBoundingBox child = getChild(vertex);
                if (child != null) {
                    /**
                     * Add a triangle to the found child
                     */
                    child.addTriangle(triangle);
                }
            }
        }

        if (triangleBelongsTo) {
            triangles.add(triangle);
        }
    }

    public Triangle getTriangleIntersectingWithRay(
        Ray ray,
        RayTriangleIntersectionSolver rayTriangleIntersectionSolver
    ) {
        double intersectionDistance = getIntersectionDistanceWithRay(ray);

        if (intersectionDistance == -1) {
            return null;
        }

        List<TriangleBoundingBox> childrenIntersectingWithRaySortedByDistance =
            getChildrenIntersectingWithRaySortedByDistance(ray);

        if (childrenIntersectingWithRaySortedByDistance.size() > 0) {
            for (TriangleBoundingBox child : childrenIntersectingWithRaySortedByDistance) {
                Triangle triangleIntersectingWithRay = child.getTriangleIntersectingWithRay(
                    ray,
                    rayTriangleIntersectionSolver
                );

                if (triangleIntersectingWithRay != null) {
                    return triangleIntersectingWithRay;
                }
            }
        }

        double minIntersectionDistanceWithRay = -1;
        Triangle triangleIntersectingWithRay = null;

        for (Triangle triangle : triangles) {
            double triangleIntersectionDistanceWithRay = rayTriangleIntersectionSolver.getRayTriangleIntersectionDistance(ray, triangle);

            if (triangleIntersectionDistanceWithRay == -1) {
                continue;
            } else if (
                minIntersectionDistanceWithRay == -1 ||
                triangleIntersectionDistanceWithRay < minIntersectionDistanceWithRay
            ) {
                minIntersectionDistanceWithRay = triangleIntersectionDistanceWithRay;
                triangleIntersectingWithRay = triangle;
            }
        }

        return triangleIntersectingWithRay;
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "vertex1=" + vertex1 +
                ", vertex2=" + vertex2 +
                ", triangles=" + triangles +
                ", children=" + children +
                '}';
    }
}