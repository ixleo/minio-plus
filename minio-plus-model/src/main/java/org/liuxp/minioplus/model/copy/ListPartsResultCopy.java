package org.liuxp.minioplus.model.copy;

import io.minio.messages.ListPartsResult;
import io.minio.messages.Part;

import java.util.ArrayList;
import java.util.List;

public class ListPartsResultCopy extends ListPartsResult {

    public List<Part> partList() {
        return new ArrayList<>();
    }

}
