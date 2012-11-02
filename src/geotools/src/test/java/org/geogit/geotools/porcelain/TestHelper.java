package org.geogit.geotools.porcelain;

import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Serializable;

import org.geotools.data.AbstractDataStoreFactory;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.primitive.Point;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class TestHelper {

    public static AbstractDataStoreFactory createTestFactory() throws Exception {

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.add("geom", Point.class);
        builder.add("label", String.class);
        builder.setName("table1");
        SimpleFeatureType type = builder.buildFeatureType();

        SimpleFeatureTypeBuilder builder2 = new SimpleFeatureTypeBuilder();
        builder2.add("geom", Point.class);
        builder2.add("name", String.class);
        builder2.setName("table2");
        SimpleFeatureType type2 = builder2.buildFeatureType();

        GeometryFactory gf = new GeometryFactory();
        SimpleFeature f1 = SimpleFeatureBuilder.build(type,
                new Object[] { gf.createPoint(new Coordinate(5, 8)), "feature1" }, null);
        SimpleFeature f2 = SimpleFeatureBuilder.build(type,
                new Object[] { gf.createPoint(new Coordinate(5, 4)), "feature2" }, null);
        SimpleFeature f3 = SimpleFeatureBuilder.build(type2,
                new Object[] { gf.createPoint(new Coordinate(3, 2)), "feature3" }, null);

        MemoryDataStore testDataStore = new MemoryDataStore();
        testDataStore.addFeature(f1);
        testDataStore.addFeature(f2);
        testDataStore.addFeature(f3);

        final AbstractDataStoreFactory factory = mock(AbstractDataStoreFactory.class);
        when(factory.createDataStore(anyMapOf(String.class, Serializable.class))).thenReturn(
                testDataStore);
        when(factory.canProcess(anyMapOf(String.class, Serializable.class))).thenReturn(true);

        return factory;
    }

    public static AbstractDataStoreFactory createEmptyTestFactory() throws Exception {

        MemoryDataStore testDataStore = new MemoryDataStore();

        final AbstractDataStoreFactory factory = mock(AbstractDataStoreFactory.class);
        when(factory.createDataStore(anyMapOf(String.class, Serializable.class))).thenReturn(
                testDataStore);
        when(factory.canProcess(anyMapOf(String.class, Serializable.class))).thenReturn(true);

        return factory;
    }

    public static AbstractDataStoreFactory createNullTestFactory() throws Exception {

        final AbstractDataStoreFactory factory = mock(AbstractDataStoreFactory.class);
        when(factory.createDataStore(anyMapOf(String.class, Serializable.class))).thenReturn(null);
        when(factory.canProcess(anyMapOf(String.class, Serializable.class))).thenReturn(true);

        return factory;
    }

    public static AbstractDataStoreFactory createFactoryWithGetNamesException() throws Exception {

        MemoryDataStore testDataStore = mock(MemoryDataStore.class);
        when(testDataStore.getNames()).thenThrow(new IOException());

        final AbstractDataStoreFactory factory = mock(AbstractDataStoreFactory.class);
        when(factory.createDataStore(anyMapOf(String.class, Serializable.class))).thenReturn(
                testDataStore);
        when(factory.canProcess(anyMapOf(String.class, Serializable.class))).thenReturn(true);

        return factory;
    }

    public static AbstractDataStoreFactory createFactoryWithGetFeatureSourceException()
            throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.add("geom", Point.class);
        builder.add("label", String.class);
        builder.setName("table1");
        SimpleFeatureType type = builder.buildFeatureType();

        SimpleFeatureTypeBuilder builder2 = new SimpleFeatureTypeBuilder();
        builder2.add("geom", Point.class);
        builder2.add("name", String.class);
        builder2.setName("table2");
        SimpleFeatureType type2 = builder2.buildFeatureType();

        GeometryFactory gf = new GeometryFactory();
        SimpleFeature f1 = SimpleFeatureBuilder.build(type,
                new Object[] { gf.createPoint(new Coordinate(5, 8)), "feature1" }, null);
        SimpleFeature f2 = SimpleFeatureBuilder.build(type,
                new Object[] { gf.createPoint(new Coordinate(5, 4)), "feature2" }, null);
        SimpleFeature f3 = SimpleFeatureBuilder.build(type2,
                new Object[] { gf.createPoint(new Coordinate(3, 2)), "feature3" }, null);

        MemoryDataStore testDataStore = new MemoryDataStore();
        testDataStore.addFeature(f1);
        testDataStore.addFeature(f2);
        testDataStore.addFeature(f3);

        MemoryDataStore spyDataStore = spy(testDataStore);

        when(spyDataStore.getFeatureSource("table1")).thenThrow(new IOException("Exception"));

        final AbstractDataStoreFactory factory = mock(AbstractDataStoreFactory.class);
        when(factory.createDataStore(anyMapOf(String.class, Serializable.class))).thenReturn(
                spyDataStore);
        when(factory.canProcess(anyMapOf(String.class, Serializable.class))).thenReturn(true);

        return factory;
    }
}