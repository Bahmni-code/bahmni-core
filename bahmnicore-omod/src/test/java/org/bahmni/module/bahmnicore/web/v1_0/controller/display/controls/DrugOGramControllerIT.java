package org.bahmni.module.bahmnicore.web.v1_0.controller.display.controls;

import org.bahmni.module.bahmnicore.web.v1_0.BaseIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.bahmniemrapi.drugogram.contract.Regimen;
import org.openmrs.module.bahmniemrapi.drugogram.contract.RegimenRow;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DrugOGramControllerIT extends BaseIntegrationTest {
    @Autowired
    DrugOGramController drugOGramController;

    @Before
    public void setUp() throws Exception {
        executeDataSet("drugogram.xml");
        executeDataSet("revisedDrugsForDrugOGram.xml");
        executeDataSet("discontinueDrugsForDrugOGram.xml");
        executeDataSet("startAndStopOnSameDateDrugs.xml");
    }

    @Test
    public void shouldFetchDrugsInRegimenTableFormat() throws Exception {
        Regimen regimen = drugOGramController.getRegimen("1a246ed5-3c11-11de-a0ba-001ed98eb67a", null);

        assertNotNull(regimen);
        assertEquals(2, regimen.getHeaders().size());
        assertEquals(3, regimen.getRows().size());
        Iterator<RegimenRow> rowIterator = regimen.getRows().iterator();

        RegimenRow firstRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-23 08:00:00")), firstRow.getDate());
        assertEquals("1000.0", firstRow.getDrugs().get("Ibuprofen"));
        assertEquals("450.0", firstRow.getDrugs().get("Crocin"));

        RegimenRow secondRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-26 00:00:00.0")), secondRow.getDate());
        assertEquals("1000.0", secondRow.getDrugs().get("Ibuprofen"));
        assertEquals("STOP", secondRow.getDrugs().get("Crocin"));

        RegimenRow thirdRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-30 00:00:00.0")), thirdRow.getDate());
        assertEquals("STOP", thirdRow.getDrugs().get("Ibuprofen"));
        assertEquals(null, thirdRow.getDrugs().get("Crocin"));
    }

    @Test
    public void shouldFetchSpecifiedDrugsInRegimenTableFormat() throws Exception {
        Regimen regimen = drugOGramController.getRegimen("1a246ed5-3c11-11de-a0ba-001ed98eb67a", Arrays.asList("Ibuprofen"));

        assertNotNull(regimen);
        assertEquals(1, regimen.getHeaders().size());
        assertEquals(2, regimen.getRows().size());
        Iterator<RegimenRow> rowIterator = regimen.getRows().iterator();

        RegimenRow firstRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-23 08:00:00")), firstRow.getDate());
        assertEquals("1000.0", firstRow.getDrugs().get("Ibuprofen"));
        assertEquals(false, firstRow.getDrugs().keySet().contains("Crocin"));

        RegimenRow thirdRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-30 00:00:00.0")), thirdRow.getDate());
        assertEquals("STOP", thirdRow.getDrugs().get("Ibuprofen"));
        assertEquals(false, firstRow.getDrugs().keySet().contains("Crocin"));
    }

    @Test
    public void shouldFetchSpecifiedDrugsWhenWeSpecifyConceptSetNameInRegimenTableFormat() throws Exception {
        Regimen regimen = drugOGramController.getRegimen("1a246ed5-3c11-11de-a0ba-001ed98eb67a", Arrays.asList("TB Drugs"));

        assertNotNull(regimen);
        assertEquals(1, regimen.getHeaders().size());
        assertEquals(2, regimen.getRows().size());
        Iterator<RegimenRow> rowIterator = regimen.getRows().iterator();

        RegimenRow firstRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-23 09:00:00")), firstRow.getDate());
        assertEquals("450.0", firstRow.getDrugs().get("Crocin"));

        RegimenRow thirdRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-26 00:00:00.0")), thirdRow.getDate());
        assertEquals("STOP", thirdRow.getDrugs().get("Crocin"));
    }

    @Test
    public void shouldFetchRevisedDrugsInRegimenTableFormat() throws Exception {
        Regimen regimen = drugOGramController.getRegimen("1a246ed5-3c11-11de-a0ba-001edc8eb67a", null);

        assertNotNull(regimen);
        assertEquals(2, regimen.getHeaders().size());
        assertEquals(4, regimen.getRows().size());
        Iterator<RegimenRow> rowIterator = regimen.getRows().iterator();

        RegimenRow firstRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-23 08:00:00")), firstRow.getDate());
        assertEquals("1000.0", firstRow.getDrugs().get("Ibuprofen"));
        assertEquals("450.0", firstRow.getDrugs().get("Crocin"));

        RegimenRow secondRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-25 08:00:00")), secondRow.getDate());
        assertEquals("500.0", secondRow.getDrugs().get("Ibuprofen"));
        assertEquals("450.0", secondRow.getDrugs().get("Crocin"));

        RegimenRow thirdRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-26 00:00:00.0")), thirdRow.getDate());
        assertEquals("500.0", thirdRow.getDrugs().get("Ibuprofen"));
        assertEquals("STOP", thirdRow.getDrugs().get("Crocin"));

        RegimenRow fourthRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-10-02 00:00:00.0")), fourthRow.getDate());
        assertEquals("STOP", fourthRow.getDrugs().get("Ibuprofen"));
        assertEquals(null , fourthRow.getDrugs().get("Crocin"));
    }

    @Test
    public void shouldFetchDiscontinueDrugsInRegimenTableFormat() throws Exception {
        Regimen regimen = drugOGramController.getRegimen("1a246ed5-3c11-11de-a0ba-001edxseb67a", null);

        assertNotNull(regimen);
        assertEquals(2, regimen.getHeaders().size());
        assertEquals(4, regimen.getRows().size());
        Iterator<RegimenRow> rowIterator = regimen.getRows().iterator();

        RegimenRow firstRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-23 08:00:00")), firstRow.getDate());
        assertEquals("1000.0", firstRow.getDrugs().get("Ibuprofen"));
        assertEquals(null, firstRow.getDrugs().get("Crocin"));

        RegimenRow secondRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-23 08:00:00")), secondRow.getDate());
        assertEquals("STOP", secondRow.getDrugs().get("Ibuprofen"));
        assertEquals(null, secondRow.getDrugs().get("Crocin"));

        RegimenRow thirdRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-25 00:00:00.0")), thirdRow.getDate());
        assertEquals(null, thirdRow.getDrugs().get("Ibuprofen"));
        assertEquals("450.0", thirdRow.getDrugs().get("Crocin"));

        RegimenRow fourthRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-26 00:00:00.0")), fourthRow.getDate());
        assertEquals(null, fourthRow.getDrugs().get("Ibuprofen"));
        assertEquals("STOP", fourthRow.getDrugs().get("Crocin"));
    }

    @Test
    public void shouldFetchOrdersWhichAreStartedAndStoppedOnSameDate() throws Exception {
        Regimen regimen = drugOGramController.getRegimen("1a246ed5-3c11-11de-a0ba-001djxseb67a", null);

        assertNotNull(regimen);
        assertEquals(3, regimen.getHeaders().size());
        assertEquals(5, regimen.getRows().size());
        Iterator<RegimenRow> rowIterator = regimen.getRows().iterator();
        RegimenRow firstRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-23 08:00:00")), firstRow.getDate());
        assertEquals("1000.0", firstRow.getDrugs().get("Ibuprofen"));
        assertEquals("450.0", firstRow.getDrugs().get("Crocin"));
        assertEquals(null, firstRow.getDrugs().get("Paracetamol"));

        RegimenRow secondRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-23 08:00:00")), secondRow.getDate());
        assertEquals("STOP", secondRow.getDrugs().get("Ibuprofen"));
        assertEquals("450.0", secondRow.getDrugs().get("Crocin"));
        assertEquals(null, secondRow.getDrugs().get("Paracetamol"));

        RegimenRow thirdRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-24 09:00:00")), thirdRow.getDate());
        assertEquals(null, thirdRow.getDrugs().get("Ibuprofen"));
        assertEquals("450.0", thirdRow.getDrugs().get("Crocin"));
        assertEquals("40.0", thirdRow.getDrugs().get("Paracetamol"));

        RegimenRow fourthRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-25 00:00:00.0")), fourthRow.getDate());
        assertEquals(null, fourthRow.getDrugs().get("Ibuprofen"));
        assertEquals("STOP", fourthRow.getDrugs().get("Crocin"));
        assertEquals("40.0", fourthRow.getDrugs().get("Paracetamol"));

        RegimenRow fifthRow = rowIterator.next();
        assertEquals(getOnlyDate(stringToDate("2005-09-28 00:00:00.0")), fifthRow.getDate());
        assertEquals(null, fifthRow.getDrugs().get("Ibuprofen"));
        assertEquals(null, fifthRow.getDrugs().get("Crocin"));
        assertEquals("STOP", fifthRow.getDrugs().get("Paracetamol"));
    }

    public Date getOnlyDate(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(sdf.format(date));
    }

    public Date stringToDate(String dateString) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return format.parse(dateString);
    }

}