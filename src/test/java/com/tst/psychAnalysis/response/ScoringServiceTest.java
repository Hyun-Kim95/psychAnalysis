package com.tst.psychAnalysis.response;

import com.tst.psychAnalysis.assessment.Assessment;
import com.tst.psychAnalysis.assessment.Item;
import com.tst.psychAnalysis.assessment.Norm;
import com.tst.psychAnalysis.assessment.NormRepository;
import com.tst.psychAnalysis.assessment.Scale;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScoringServiceTest {

    @Test
    void score_appliesReverseScoringWeightAndTScore() throws Exception {
        ItemResponseRepository itemResponseRepository = mock(ItemResponseRepository.class);
        NormRepository normRepository = mock(NormRepository.class);
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

        ScoringService service = new ScoringService(itemResponseRepository, normRepository, objectMapper);

        Assessment assessment = new Assessment();
        java.lang.reflect.Field nameField = Assessment.class.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(assessment, "Test");

        Scale scale = new Scale();
        java.lang.reflect.Field assessmentField = Scale.class.getDeclaredField("assessment");
        assessmentField.setAccessible(true);
        assessmentField.set(scale, assessment);
        java.lang.reflect.Field codeField = Scale.class.getDeclaredField("code");
        codeField.setAccessible(true);
        codeField.set(scale, "ANX");

        Item item1 = new Item();
        setItemFields(item1, assessment, scale, 1, false, 1.0);

        Item item2 = new Item();
        setItemFields(item2, assessment, scale, 2, true, 1.2);

        ResponseSession session = new ResponseSession();
        session.setId(UUID.randomUUID());
        session.setAssessment(assessment);
        session.setGroupCode("A");
        session.setStartedAt(LocalDateTime.now());

        ItemResponse ir1 = new ItemResponse();
        ir1.setResponseSession(session);
        ir1.setItem(item1);
        ir1.setRawValue(4); // 점수 4

        ItemResponse ir2 = new ItemResponse();
        ir2.setResponseSession(session);
        ir2.setItem(item2);
        ir2.setRawValue(2); // 역채점 -> 4, 가중치 1.2 -> 4.8

        when(itemResponseRepository.findAll()).thenReturn(List.of(ir1, ir2));

        Norm normScale = new Norm();
        setNormFields(normScale, assessment, scale, null, 10.0, 2.0);

        Norm normTotal = new Norm();
        setNormFields(normTotal, assessment, null, null, 20.0, 4.0);

        when(normRepository.findByAssessmentId(null)).thenReturn(List.of(normScale, normTotal));

        Result result = service.score(session);

        // raw: 4 + 4.8 = 8.8
        assertThat(result.getTotalRawScore()).isEqualTo(8.8);

        // T-score 기준값이 있으므로 null 이 아니어야 함
        assertThat(result.getTotalTScore()).isNotNull();
    }

    private void setItemFields(Item item, Assessment assessment, Scale scale,
                               int itemNumber, boolean reverse, double weight) throws Exception {
        java.lang.reflect.Field assessmentField = Item.class.getDeclaredField("assessment");
        assessmentField.setAccessible(true);
        assessmentField.set(item, assessment);

        java.lang.reflect.Field scaleField = Item.class.getDeclaredField("scale");
        scaleField.setAccessible(true);
        scaleField.set(item, scale);

        java.lang.reflect.Field itemNumberField = Item.class.getDeclaredField("itemNumber");
        itemNumberField.setAccessible(true);
        itemNumberField.setInt(item, itemNumber);

        java.lang.reflect.Field reverseField = Item.class.getDeclaredField("isReverseScored");
        reverseField.setAccessible(true);
        reverseField.setBoolean(item, reverse);

        java.lang.reflect.Field weightField = Item.class.getDeclaredField("weight");
        weightField.setAccessible(true);
        weightField.setDouble(item, weight);
    }

    private void setNormFields(Norm norm, Assessment assessment, Scale scale,
                               String groupCode, double mean, double sd) throws Exception {
        java.lang.reflect.Field assessmentField = Norm.class.getDeclaredField("assessment");
        assessmentField.setAccessible(true);
        assessmentField.set(norm, assessment);

        java.lang.reflect.Field scaleField = Norm.class.getDeclaredField("scale");
        scaleField.setAccessible(true);
        scaleField.set(norm, scale);

        java.lang.reflect.Field groupField = Norm.class.getDeclaredField("groupCode");
        groupField.setAccessible(true);
        groupField.set(norm, groupCode);

        java.lang.reflect.Field meanField = Norm.class.getDeclaredField("mean");
        meanField.setAccessible(true);
        meanField.setDouble(norm, mean);

        java.lang.reflect.Field sdField = Norm.class.getDeclaredField("sd");
        sdField.setAccessible(true);
        sdField.setDouble(norm, sd);
    }
}

