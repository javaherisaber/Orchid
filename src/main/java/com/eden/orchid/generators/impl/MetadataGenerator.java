package com.eden.orchid.generators.impl;

import com.eden.common.json.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.generators.Generator;
import com.eden.orchid.resources.OrchidPage;
import com.eden.orchid.resources.OrchidReference;
import com.eden.orchid.resources.impl.StringResource;
import com.eden.orchid.utilities.AutoRegister;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@AutoRegister
public class MetadataGenerator implements Generator {

    private List<OrchidPage> meta;

    @Override
    public int priority() {
        return 10;
    }

    @Override
    public String getName() {
        return "meta";
    }

    @Override
    public JSONElement startIndexing() {
        JSONObject siteAssets = new JSONObject();

        meta = new ArrayList<>();

        StringResource resource = new StringResource("", new OrchidReference("meta/classIndex.json"));
        resource.setEmbeddedData(Orchid.query("index.classes.internal"));

        OrchidPage entry = new OrchidPage(resource);

        JSONObject index = new JSONObject();
        index.put("name", entry.getReference().getTitle());
        index.put("title", entry.getReference().getTitle());
        index.put("url", entry.getReference().toString());

        meta.add(entry);

        OrchidUtils.buildTaxonomy(resource, siteAssets, index);

        return new JSONElement(siteAssets.getJSONObject("assets"));
    }

    @Override
    public void startGeneration() {
        for (OrchidPage metaPage : meta) {
            metaPage.renderRawContent();
        }
    }
}
