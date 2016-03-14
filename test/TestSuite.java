import model.MapInteractionModelTest;
import model.MapModelTest;
import model.NameSearchModelTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import util.ConverterTest;
import util.DijkstraTest;
import util.QuadTreeTest;
import util.TrieTest;
import util.RectangleTest;
import util.graph.GraphDataLoadTest;
import util.graph.GraphTest;
import util.UTMConverterTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	MapInteractionModelTest.class,
	MapModelTest.class,
	NameSearchModelTest.class,
	ConverterTest.class,
	DijkstraTest.class,
	QuadTreeTest.class,
	TrieTest.class,
	RectangleTest.class,
	GraphDataLoadTest.class,
	GraphTest.class,
	UTMConverterTest.class
})
public class TestSuite {
    // the class remains completely empty, 
    // being used only as a holder for the above annotations
}