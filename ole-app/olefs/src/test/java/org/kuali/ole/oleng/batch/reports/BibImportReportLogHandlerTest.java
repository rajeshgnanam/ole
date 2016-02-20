package org.kuali.ole.oleng.batch.reports;

import org.apache.camel.Processor;
import org.junit.Test;
import org.kuali.ole.docstore.common.response.BibResponse;
import org.kuali.ole.docstore.common.response.HoldingsResponse;
import org.kuali.ole.docstore.common.response.ItemResponse;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.ole.oleng.batch.reports.processors.SummaryReportProcessor;
import org.kuali.ole.utility.MarcRecordUtil;
import org.marc4j.marc.Record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by SheikS on 2/18/2016.
 */
public class BibImportReportLogHandlerTest {

    @Test
    public void testLogMessage() throws Exception {
        OleNGBibImportResponse oleNGBibImportResponse = buildOleNGBibImportResponse();
        oleNGBibImportResponse.setDirectoryName("1");
        MarcRecordUtil marcRecordUtil = new MarcRecordUtil();
        Record record = marcRecordUtil.getMarcFactory().newRecord();
        oleNGBibImportResponse.setMatchedRecords(Collections.singletonList(record));
        oleNGBibImportResponse.setUnmatchedRecords(Collections.singletonList(record));
        oleNGBibImportResponse.setMultipleMatchedRecords(Collections.singletonList(record));
        BibImportReportLogHandler bibImportReportLogHandler1 = new MockBibImportReportLogHandler(oleNGBibImportResponse.getDirectoryName(),"profile1");
        bibImportReportLogHandler1.logMessage(oleNGBibImportResponse);


        oleNGBibImportResponse.setDirectoryName("2");
        BibImportReportLogHandler bibImportReportLogHandler2 = new MockBibImportReportLogHandler(oleNGBibImportResponse.getDirectoryName(),"profile2");
        bibImportReportLogHandler2.logMessage(oleNGBibImportResponse);


        oleNGBibImportResponse.setDirectoryName("");
        BibImportReportLogHandler bibImportReportLogHandler3 = new MockBibImportReportLogHandler(oleNGBibImportResponse.getDirectoryName(),"profile3");
        bibImportReportLogHandler3.logMessage(oleNGBibImportResponse);
        Thread.sleep(5000);
    }

    private OleNGBibImportResponse buildOleNGBibImportResponse() {
        OleNGBibImportResponse oleNGBibImportResponse = new OleNGBibImportResponse();
        List<ItemResponse> itemResponses = new ArrayList<>();
        List<HoldingsResponse> holdingsResponses = new ArrayList<>();
        List<BibResponse> bibResponses = new ArrayList<>();
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setItemId("wio-300001");
        itemResponse.setOperation("Created");
        itemResponses.add(itemResponse);
        HoldingsResponse holdingsResponse = new HoldingsResponse();
        holdingsResponse.setHoldingsId("who-200001");
        holdingsResponse.setOperation("Created");
        holdingsResponse.setItemResponses(itemResponses);
        holdingsResponses.add(holdingsResponse);
        BibResponse bibResponse = new BibResponse();
        bibResponse.setBibId("wbm-100001");
        bibResponse.setOperation("Created");
        bibResponse.setHoldingsResponses(holdingsResponses);
        bibResponses.add(bibResponse);
        oleNGBibImportResponse.setBibResponses(bibResponses);
        return oleNGBibImportResponse;
    }



    public class MockBibImportReportLogHandler extends BibImportReportLogHandler {

        public MockBibImportReportLogHandler(String directoryName, String profileName) {
            super(directoryName, profileName);
        }

        @Override
        public String getReportDirectoryPath() {
            String tempLocation = System.getProperty("java.io.tmpdir");
            System.out.println("Temp dir : " + tempLocation);
            return tempLocation;
        }

        @Override
        public List<Processor> getProcessors() {
            List<Processor> processors = new ArrayList<>();
            processors.add(new SummaryReportProcessor());
            return processors;
        }
    }
}