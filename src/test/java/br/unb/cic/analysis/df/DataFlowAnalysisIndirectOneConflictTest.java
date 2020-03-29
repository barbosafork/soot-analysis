package br.unb.cic.analysis.df;

import br.unb.cic.analysis.AbstractMergeConflictDefinition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import soot.*;
import soot.toolkits.graph.ExceptionalUnitGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFlowAnalysisIndirectOneConflictTest {

    private DataFlowAnalysis analysis;

    @Before
    public void configure() {
        G.reset();
        Collector.instance().clear();

        AbstractMergeConflictDefinition definition = new AbstractMergeConflictDefinition() {
            @Override
            protected Map<String, List<Integer>> sourceDefinitions() {
                Map<String, List<Integer>> res = new HashMap<>();
                List<Integer> lines = new ArrayList<>();
                lines.add(7);
                res.put("br.unb.cic.analysis.samples.IntraproceduralIndirectSource", lines);
                return res;
            }

            @Override
            protected Map<String, List<Integer>> sinkDefinitions() {
                Map<String, List<Integer>> res = new HashMap<>();
                List<Integer> lines = new ArrayList<>();
                lines.add(12);
                res.put("br.unb.cic.analysis.samples.IntraproceduralIndirectSource", lines);
                return res;
            }
        };

        PackManager.v().getPack("jtp").add(
		    new Transform("jtp.oneConflict", new BodyTransformer() {
				@Override
				protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
					analysis = new DataFlowAnalysis(new ExceptionalUnitGraph(body), definition);
				}
            		    }));
        String cp = "target/test-classes";
        String targetClass = "br.unb.cic.analysis.samples.IntraproceduralIndirectSource";

        Main.main(new String[] {"-w", "-allow-phantom-refs", "-f", "J", "-keep-line-number", "-cp", cp, targetClass});
    }

    @Test
    public void testDataFlowAnalysisExpectingOneConflict() {
       // Assert.assertNotNull(analysis);
       // Assert.assertNotNull(analysis.getConflicts());
        Assert.assertEquals(0, analysis.getConflicts().size());
    }
}