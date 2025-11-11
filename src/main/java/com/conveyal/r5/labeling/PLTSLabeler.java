package com.conveyal.r5.labeling;

import com.conveyal.osmlib.OSMEntity;
import com.conveyal.r5.streets.EdgeStore.Edge;
import com.conveyal.r5.streets.VertexStore.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Label streets with Pedestrian Level of Traffic Stress values.
 * Unlike the bike LTS labeling in LevelOfTrafficStressLabeler, this PLTS labeling process does no
 * imputation or estimation of stress values — instead, it assumes that the values are already set
 * in the source OSM data. The process simply looks at the specified tags for nodes and ways and
 * assigns the values to vertices and edges. A default value is used when no PLTS value is available
 * in the OSM data.
 */

public class PLTSLabeler {
    private static final Logger LOG = LoggerFactory.getLogger(PLTSLabeler.class);

    private String pltsTag = "plts";
    private byte defaultPLTSValue = Byte.MAX_VALUE;

    public void setPLTSTag(String pltsTag) {
        this.pltsTag = pltsTag;
    }

    public void setDefaultPLTSValue(byte defaultPLTSValue) {
        this.defaultPLTSValue = defaultPLTSValue;
    }

    private byte pltsValueFromOSMEntity(OSMEntity osmEntity) {
        byte pltsValue = defaultPLTSValue;
        String ltsTagValue = osmEntity.getTag(pltsTag);
        if (ltsTagValue != null) {
            pltsValue = Byte.parseByte(ltsTagValue);
        }
        if (pltsValue < 1) {
            LOG.error("Invalid PLTS value (" + pltsValue + "). Using default (" + defaultPLTSValue + "0");
            pltsValue = defaultPLTSValue;
        }
        return pltsValue;
    }

    public void label (OSMEntity osmEntity, Edge edge) {
        edge.setPLTS(pltsValueFromOSMEntity(osmEntity));
    }

    public void label (OSMEntity osmEntity, Vertex vertex) {
        vertex.setPLTS(pltsValueFromOSMEntity(osmEntity));
    }
}