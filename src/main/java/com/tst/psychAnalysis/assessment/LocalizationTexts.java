package com.tst.psychAnalysis.assessment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class LocalizationTexts {

    private static final Map<String, String> ASSESSMENT_NAMES_EN = buildAssessmentNamesEn();

    private static Map<String, String> buildAssessmentNamesEn() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("BDI 우울검사", "BDI Depression Test");
        m.put("BDI 우울검사 (상세)", "BDI Depression Test (Detailed)");
        m.put("BAI 불안검사", "BAI Anxiety Test");
        m.put("BAI 불안검사 (상세)", "BAI Anxiety Test (Detailed)");
        m.put("회복탄력성 검사", "Resilience Test");
        m.put("회복탄력성 검사 (상세)", "Resilience Test (Detailed)");
        m.put("NEO 성격검사", "NEO Personality Test");
        m.put("NEO 성격검사 (상세)", "NEO Personality Test (Detailed)");
        m.put("TCI 기질성격검사", "TCI Temperament & Character Inventory");
        m.put("TCI 기질성격검사 (상세)", "TCI Temperament & Character Inventory (Detailed)");
        // DB 마이그레이션에 쓰인 실제 검사명
        m.put("TCI 검사", "TCI Temperament & Character Inventory");
        m.put("TCI 검사 (상세)", "TCI Temperament & Character Inventory (Detailed)");
        return Map.copyOf(m);
    }

    private static final Map<String, String> SCALE_NAMES_EN = buildScaleNamesEn();
    private static final Map<Integer, String> BDI_ITEMS_EN = buildBdiItemsEn();
    private static final Map<Integer, String> BAI_ITEMS_EN = buildBaiItemsEn();
    private static final Map<String, String> TCI_SHORT_ITEMS_EN = buildTciShortItemsEn();
    private static final Map<String, String> NEO_SHORT_ITEMS_EN = buildNeoShortItemsEn();
    private static final Map<String, String> TCI_DETAIL_ITEMS_EN = buildTciDetailItemsEn();
    private static final Map<String, String> NEO_DETAIL_ITEMS_EN = buildNeoDetailItemsEn();
    private static final Map<Integer, String> RESILIENCE_ITEMS_EN = buildResilienceItemsEn();

    private static Map<String, String> buildScaleNamesEn() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("TOTAL", "Total");
        m.put("R", "Resilience");
        m.put("D", "Depression");
        m.put("A", "Anxiety");
        m.put("NS", "Novelty Seeking");
        m.put("HA", "Harm Avoidance");
        m.put("RD", "Reward Dependence");
        m.put("P", "Persistence");
        m.put("SD", "Self-Directedness");
        m.put("C", "Cooperativeness");
        m.put("ST", "Self-Transcendence");
        m.put("N", "Neuroticism");
        m.put("E", "Extraversion");
        m.put("O", "Openness");
        m.put("A", "Agreeableness");
        m.put("C", "Conscientiousness");

        m.put("N1", "Anxiety"); m.put("N2", "Hostility"); m.put("N3", "Depression"); m.put("N4", "Impulsiveness");
        m.put("N5", "Social Withdrawal"); m.put("N6", "Emotional Shock"); m.put("N7", "Vulnerability");
        m.put("N8", "Eccentricity"); m.put("N9", "Antisociality"); m.put("N10", "Self-Esteem");
        m.put("E1", "Sociability"); m.put("E2", "Dominance"); m.put("E3", "Sensation Seeking"); m.put("E4", "Activity");
        m.put("O1", "Creativity"); m.put("O2", "Aesthetics"); m.put("O3", "Emotionality"); m.put("O4", "Initiative");
        m.put("A1", "Warmth"); m.put("A2", "Trust"); m.put("A3", "Empathy"); m.put("A4", "Tolerance"); m.put("A5", "Humility");
        m.put("C1", "Competence"); m.put("C2", "Achievement Motivation"); m.put("C3", "Orderliness");
        m.put("C4", "Responsibility"); m.put("C5", "Self-Control"); m.put("C6", "Deliberation");
        // KRQ-53 회복탄력성 (Krq53MigrationRunner 하위척도 코드)
        m.put("ER", "Emotion regulation");
        m.put("IC", "Impulse control");
        m.put("CA", "Causal analysis");
        m.put("CO", "Communication");
        m.put("EM", "Empathy");
        m.put("SE", "Relatedness / support");
        m.put("OP", "Optimism / perceived control");
        m.put("LS", "Life satisfaction");
        m.put("GR", "Gratitude");
        return m;
    }

    public static boolean isEnglish(String acceptLanguage) {
        return acceptLanguage != null && acceptLanguage.toLowerCase().startsWith("en");
    }

    /**
     * PDF·직접 링크 등: URL {@code ?lang=en} 이 있으면 최우선.
     * (브라우저 Accept-Language 가 ko 로 시작하면 헤더만 쓸 때 영어 UI와 불일치함)
     */
    public static boolean englishFromAcceptLanguageOrLangParam(String acceptLanguage, String langQuery) {
        if (langQuery != null && !langQuery.isBlank()) {
            return isEnglish(langQuery);
        }
        return isEnglish(acceptLanguage);
    }

    /** access_log_count.event_type 저장값 → 화면 표시용 (한글 저장 레거시 지원) */
    public static String accessLogEventLabel(String storedEventType, boolean english) {
        if (storedEventType == null) {
            return "";
        }
        return switch (storedEventType) {
            case "검사 시작" -> english ? "Session started" : "검사 시작";
            case "검사 제출" -> english ? "Session submitted" : "검사 제출";
            default -> storedEventType;
        };
    }

    public static String assessmentName(String original, boolean english) {
        if (!english || original == null) return original;
        return ASSESSMENT_NAMES_EN.getOrDefault(original, original);
    }

    /**
     * 검사 설명: DB 원문은 한글 위주이므로, 영어 요청 시 검사별로 고정 영어 문구를 씁니다.
     */
    public static String assessmentDescription(String assessmentName, String dbDescription, boolean english) {
        if (!english) {
            return dbDescription != null ? dbDescription : "";
        }
        if (assessmentName == null) {
            return "";
        }
        if (assessmentName.startsWith("BDI")) {
            if (assessmentName.contains("상세")) {
                return "Beck Depression Inventory (BDI-II). 21 items, multiple choice. About 5–9 minutes.";
            }
            return "Beck Depression Inventory (BDI) short form. 11 items. About 3–4 minutes.";
        }
        if (assessmentName.startsWith("BAI")) {
            if (assessmentName.contains("상세")) {
                return "Beck Anxiety Inventory (BAI). 21 items on anxiety symptoms over the past week. About 5–7 minutes.";
            }
            return "Beck Anxiety Inventory (BAI) short form. 11 items. About 3–4 minutes.";
        }
        if (assessmentName.contains("회복탄력성")) {
            if (assessmentName.contains("상세")) {
                return "Korean Resilience Quotient (KRQ-53). 53 items, nine subscales, 5-point scale. About 15–20 minutes.";
            }
            return "Korean Resilience Quotient (KRQ) short form. 27 items (three per subscale), 5-point scale. About 8–10 minutes.";
        }
        if (assessmentName.contains("NEO")) {
            if (assessmentName.contains("상세")) {
                return "NEO Personality Inventory—Revised (adult). 58 items across Big Five facets. About 10–15 minutes.";
            }
            return "NEO personality measure for adults/college students. 29 facet scales, 29 items (one item per facet). About 7–10 minutes.";
        }
        if (assessmentName.contains("TCI")) {
            if (assessmentName.contains("상세")) {
                return "Temperament and Character Inventory (TCI), detailed version. 5-point scale. Allow about 25–35 minutes depending on pacing.";
            }
            return "Temperament and Character Inventory (TCI), short form. 5-point scale. Allow about 12–18 minutes depending on pacing.";
        }
        return dbDescription != null ? dbDescription : "";
    }

    public static String scaleName(String scaleCode, String originalName, boolean english) {
        if (!english) return originalName;
        if (scaleCode != null && SCALE_NAMES_EN.containsKey(scaleCode)) {
            return SCALE_NAMES_EN.get(scaleCode);
        }
        return originalName;
    }

    public static String groupLabel(String originalLabel, boolean english) {
        if (!english || originalLabel == null) return originalLabel;
        if (originalLabel.startsWith("N (")) return "N (Neuroticism)";
        if (originalLabel.startsWith("E (")) return "E (Extraversion)";
        if (originalLabel.startsWith("O (")) return "O (Openness)";
        if (originalLabel.startsWith("A (")) return "A (Agreeableness)";
        if (originalLabel.startsWith("C (")) return "C (Conscientiousness)";
        return originalLabel;
    }

    public static String choiceLabel(String originalLabel, boolean english) {
        if (!english || originalLabel == null) return originalLabel;
        return switch (originalLabel.trim()) {
            case "전혀 아니다" -> "Not at all";
            case "거의 아니다" -> "Rarely";
            case "보통이다" -> "Neutral";
            case "별로 그렇지 않다" -> "Slightly untrue";
            case "조금 그렇다" -> "Slightly true";
            case "전혀 느끼지 않았다" -> "Not at all";
            case "조금 느꼈다" -> "Mildly";
            case "상당히 느꼈다" -> "Moderately";
            case "심하게 느꼈다" -> "Severely";
            case "자주 그렇다" -> "Often";
            case "거의 항상 그렇다" -> "Almost always";
            case "전혀 그렇지 않다" -> "Not at all true";
            case "그렇지 않다" -> "Not true";
            case "그렇다" -> "True";
            case "매우 그렇다" -> "Very true";
            case "전혀 없음" -> "None";
            case "가끔" -> "Occasionally";
            case "자주" -> "Often";
            case "거의 항상" -> "Almost always";
            default -> originalLabel;
        };
    }

    public static String itemText(String assessmentName, String scaleCode, int itemNumber, String originalText, boolean english) {
        if (!english) return originalText;
        if (assessmentName == null) return originalText;
        if (assessmentName.contains("BDI")) {
            return BDI_ITEMS_EN.getOrDefault(itemNumber, originalText);
        }
        if (assessmentName.contains("BAI")) {
            return BAI_ITEMS_EN.getOrDefault(itemNumber, originalText);
        }
        if (assessmentName.equals("TCI 검사")) {
            return TCI_SHORT_ITEMS_EN.getOrDefault(scaleCode + "_" + itemNumber, originalText);
        }
        if (assessmentName.equals("TCI 검사 (상세)")) {
            return TCI_DETAIL_ITEMS_EN.getOrDefault(scaleCode + "_" + itemNumber, originalText);
        }
        if (assessmentName.equals("NEO 성격검사")) {
            return NEO_SHORT_ITEMS_EN.getOrDefault(scaleCode + "_" + itemNumber, originalText);
        }
        if (assessmentName.equals("NEO 성격검사 (상세)")) {
            return NEO_DETAIL_ITEMS_EN.getOrDefault(scaleCode + "_" + itemNumber, originalText);
        }
        if (assessmentName.contains("회복탄력성")) {
            return RESILIENCE_ITEMS_EN.getOrDefault(itemNumber, originalText);
        }
        return originalText;
    }

    public static Map<String, String> interpret(
            String assessmentName,
            Double totalRawScore,
            Map<String, Double> scaleRawScores,
            Map<String, Double> scaleTScores,
            boolean english
    ) {
        if (!english) {
            return ScaleInterpretationFacade.interpret(assessmentName, totalRawScore, scaleRawScores, scaleTScores);
        }
        if (assessmentName == null) return Map.of();

        if (assessmentName.contains("BDI")) {
            String text = bdiText(totalRawScore);
            return text.isEmpty() ? Map.of() : Map.of("D", text);
        }
        if (assessmentName.contains("BAI")) {
            String text = baiText(totalRawScore);
            return text.isEmpty() ? Map.of() : Map.of("A", text);
        }
        if (assessmentName.contains("회복탄력성")) {
            Double t = scaleTScores != null ? (scaleTScores.get("TOTAL") != null ? scaleTScores.get("TOTAL") : scaleTScores.get("R")) : null;
            String text = resilienceText(t);
            if (text.isEmpty()) return Map.of();
            return scaleTScores != null && scaleTScores.containsKey("TOTAL") ? Map.of("TOTAL", text) : Map.of("R", text);
        }
        if (assessmentName.contains("TCI")) {
            return genericBandInterpretation(TciScaleLabels.SCALE_ORDER, scaleTScores);
        }
        if (assessmentName.contains("NEO")) {
            boolean useFacets = scaleTScores != null && scaleTScores.keySet().stream().anyMatch(NeoScaleInterpretation.FACET_ORDER::contains);
            List<String> order = useFacets ? NeoScaleInterpretation.FACET_ORDER : NeoScaleInterpretation.SCALE_ORDER;
            return genericBandInterpretation(order, scaleTScores);
        }
        return Map.of();
    }

    public static String referenceDescription(String assessmentName, boolean english) {
        if (!english) {
            return ScaleInterpretationFacade.getReferenceDescription(assessmentName);
        }
        if (assessmentName == null) return "";
        if (assessmentName.contains("BDI")) {
            return "Total raw score range: 0-9 minimal, 10-15 mild, 16-25 moderate, 26-63 severe.";
        }
        if (assessmentName.contains("BAI")) {
            return "Total raw score range: 0-7 minimal, 8-15 mild, 16-25 moderate, 26+ severe.";
        }
        if (assessmentName.contains("TCI")) {
            return "T-score bands: low (<43), average (43-57), high (>57), interpreted per scale.";
        }
        if (assessmentName.contains("NEO")) {
            return "T-score bands: low (<=44), average (45-55), high (>=56), interpreted per facet/scale.";
        }
        if (assessmentName.contains("회복탄력성")) {
            return "T-score bands: low (<43), average (43-57), high (>57).";
        }
        return "No interpretation guidance available.";
    }

    private static String bdiText(Double totalRawScore) {
        if (totalRawScore == null || totalRawScore < 0) return "";
        int raw = totalRawScore.intValue();
        if (raw <= 9) return "Minimal depressive symptom level.";
        if (raw <= 15) return "Mild depressive symptom level.";
        if (raw <= 25) return "Moderate depressive symptom level.";
        return "Severe depressive symptom level. Professional consultation is recommended.";
    }

    private static String baiText(Double totalRawScore) {
        if (totalRawScore == null || totalRawScore < 0) return "";
        int raw = totalRawScore.intValue();
        if (raw <= 7) return "Minimal anxiety symptom level.";
        if (raw <= 15) return "Mild anxiety symptom level.";
        if (raw <= 25) return "Moderate anxiety symptom level.";
        return "Severe anxiety symptom level. Professional consultation is recommended.";
    }

    private static String resilienceText(Double tScore) {
        if (tScore == null) return "";
        if (tScore < 43) return "Resilience appears relatively low. Building support systems and coping routines may help.";
        if (tScore > 57) return "Resilience is relatively high. You tend to adapt and recover effectively.";
        return "Resilience is within the average range.";
    }

    private static Map<String, String> genericBandInterpretation(List<String> scaleOrder, Map<String, Double> scaleTScores) {
        if (scaleTScores == null) return Map.of();
        Map<String, String> out = new LinkedHashMap<>();
        for (String code : scaleOrder) {
            if (!scaleTScores.containsKey(code)) continue;
            Double t = scaleTScores.get(code);
            if (t == null) continue;
            String level = t < 43 ? "low" : (t > 57 ? "high" : "average");
            String scaleName = SCALE_NAMES_EN.getOrDefault(code, code);
            out.put(code, "The " + scaleName + " level is " + level + " (T-score: " + String.format("%.1f", t) + ").");
        }
        return out;
    }

    private static Map<Integer, String> buildBdiItemsEn() {
        Map<Integer, String> m = new LinkedHashMap<>();
        m.put(1, "Sadness");
        m.put(2, "Pessimism");
        m.put(3, "Past Failure");
        m.put(4, "Loss of Pleasure");
        m.put(5, "Guilty Feelings");
        m.put(6, "Punishment Feelings");
        m.put(7, "Self-Dislike");
        m.put(8, "Self-Criticalness");
        m.put(9, "Suicidal Thoughts or Wishes");
        m.put(10, "Crying");
        m.put(11, "Agitation");
        m.put(12, "Loss of Interest");
        m.put(13, "Indecisiveness");
        m.put(14, "Worthlessness");
        m.put(15, "Loss of Energy");
        m.put(16, "Changes in Sleeping Pattern");
        m.put(17, "Irritability");
        m.put(18, "Changes in Appetite");
        m.put(19, "Concentration Difficulty");
        m.put(20, "Tiredness or Fatigue");
        m.put(21, "Loss of Interest in Sex");
        return m;
    }

    private static Map<Integer, String> buildBaiItemsEn() {
        Map<Integer, String> m = new LinkedHashMap<>();
        m.put(1, "Numbness or tingling");
        m.put(2, "Feeling hot");
        m.put(3, "Wobbliness in legs");
        m.put(4, "Unable to relax");
        m.put(5, "Fear of worst happening");
        m.put(6, "Dizzy or lightheaded");
        m.put(7, "Heart pounding / racing");
        m.put(8, "Unsteady");
        m.put(9, "Terrified or afraid");
        m.put(10, "Nervous");
        m.put(11, "Feeling of choking");
        m.put(12, "Hands trembling");
        m.put(13, "Feeling shaky / unsteady");
        m.put(14, "Fear of losing control");
        m.put(15, "Difficulty breathing");
        m.put(16, "Fear of dying");
        m.put(17, "Scared");
        m.put(18, "Indigestion / abdominal discomfort");
        m.put(19, "Faint or lightheaded");
        m.put(20, "Face flushed");
        m.put(21, "Sweating (not due to heat)");
        return m;
    }

    private static Map<String, String> buildTciShortItemsEn() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("NS_1", "At social gatherings with new people, I blend in quickly even if I feel awkward at first.");
        m.put("NS_2", "When traveling, I enjoy adding spontaneous stops that were not planned.");
        m.put("NS_3", "I feel more energized by somewhat unpredictable days than by repetitive routines.");
        m.put("NS_4", "I tend to be the first to suggest trying new ideas that others hesitate about.");
        m.put("NS_5", "When life feels too monotonous, I intentionally look for new experiences.");
        m.put("HA_1", "In unfamiliar places or with new people, I first imagine what could go wrong.");
        m.put("HA_2", "Before important decisions, thoughts like \"What if I fail?\" come up repeatedly.");
        m.put("HA_3", "Even when others say it is fine, my worries often do not settle easily.");
        m.put("HA_4", "When unexpected situations happen, I feel anxiety before curiosity.");
        m.put("HA_5", "If I do not feel sufficiently safe, I struggle to start new challenges.");
        m.put("RD_1", "When someone sincerely listens to me, that moment stays with me for a long time.");
        m.put("RD_2", "If someone close says they feel hurt by me, it stays on my mind for a while.");
        m.put("RD_3", "Praise and recognition from others motivate me to keep going.");
        m.put("RD_4", "When making important decisions, I often think first about how others will view it.");
        m.put("RD_5", "If I feel distant from someone close, I try to restore the relationship through conversation.");
        m.put("P_1", "Even when interest drops, I try to finish what I started.");
        m.put("P_2", "Even if results are not immediate, I continue when I believe the work is meaningful.");
        m.put("P_3", "Even on low-energy days, I try to complete at least the minimum required tasks.");
        m.put("P_4", "After repeated small failures, I tend to try again with a different approach.");
        m.put("P_5", "Even if others tell me to stop, I persist when I believe it is necessary.");
        m.put("SD_1", "When making important choices, I feel the final decision should be my responsibility.");
        m.put("SD_2", "Even in difficult times, I tend to believe my life direction is ultimately my own choice.");
        m.put("SD_3", "Even if it differs from others' expectations, I try to choose the path that suits me.");
        m.put("SD_4", "I try not to easily change my own standards and values depending on circumstances.");
        m.put("SD_5", "In difficult situations, I first look at what part I can control.");
        m.put("C_1", "When someone seems to be struggling, I look for ways to help even if we are not close.");
        m.put("C_2", "In team settings, I am okay with doing a bit more if it helps the group work better.");
        m.put("C_3", "Even when opinions differ, I try to find common ground rather than win.");
        m.put("C_4", "When I fail to keep a promise, I strongly feel sorry by imagining how the other person feels.");
        m.put("C_5", "I feel uncomfortable when rules or fairness break down, even if it is not directly my issue.");
        m.put("ST_1", "When I experience nature or art, I sometimes feel awe or connection beyond words.");
        m.put("ST_2", "Difficult times have made me rethink the meaning and direction of my life.");
        m.put("ST_3", "When I am alone, I ask myself questions like \"What kind of life do I want to live?\".");
        m.put("ST_4", "I sometimes feel a strong desire to contribute to something larger than myself.");
        m.put("ST_5", "Small everyday moments (conversation, scenery, music) can feel deeply meaningful and grateful.");
        return m;
    }

    private static Map<String, String> buildNeoShortItemsEn() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("N_1", "I tend to worry first that things may go wrong.");
        m.put("N_2", "When tasks pile up or deadlines approach, I easily feel tense or anxious.");
        m.put("N_3", "Sometimes my mood drops for no clear reason, and motivation fades.");
        m.put("N_4", "I am easily hurt or irritated by small comments or situations.");
        m.put("N_5", "My mood can shift quickly, from feeling fine to heavy or anxious.");
        m.put("E_1", "At gatherings or events, I tend to start conversations and energize the atmosphere.");
        m.put("E_2", "I do not feel much burden when starting conversations or leading topics.");
        m.put("E_3", "I feel energized in situations with new people.");
        m.put("E_4", "Spending time with friends or colleagues often improves and restores my mood.");
        m.put("E_5", "I am generally comfortable speaking and moving in group settings.");
        m.put("O_1", "When I encounter new ideas or methods, I become curious and want to explore them.");
        m.put("O_2", "When reading or imagining, vivid scenes and stories form in my mind.");
        m.put("O_3", "Artworks or natural scenery often leave lasting impressions and thoughts in me.");
        m.put("O_4", "On any topic, I try to consider opposing views and different perspectives.");
        m.put("O_5", "When unfamiliar areas come up, I usually feel a desire to learn.");
        m.put("A_1", "I tend to approach people with basic trust, even when meeting them for the first time.");
        m.put("A_2", "In collaborative work, I am willing to adjust if it helps the team.");
        m.put("A_3", "I try to understand others by considering their perspective.");
        m.put("A_4", "When opinions conflict, I prefer finding harmony over fighting.");
        m.put("A_5", "If someone seems to be struggling, I look for helpful words or actions.");
        m.put("C_1", "When I have tasks to do, I try to start promptly rather than delay.");
        m.put("C_2", "For major tasks, I tend to break them into steps, plan, and execute.");
        m.put("C_3", "I try to complete assigned work and pay attention to details.");
        m.put("C_4", "When I set a goal, I tend to keep working steadily until I reach it.");
        m.put("C_5", "I try to keep promises and follow rules whenever possible.");
        return m;
    }

    private static Map<Integer, String> buildResilienceItemsEn() {
        Map<Integer, String> m = new LinkedHashMap<>();
        m.put(1, "When difficulties arise, I tend to reflect on causes and adapt my approach to overcome them.");
        m.put(2, "After failure, I usually think \"Let's try once more\" and attempt again.");
        m.put(3, "Even under stress, I adapt by taking breaks or splitting tasks.");
        m.put(4, "Even in hard times, I try to keep believing things will work out.");
        m.put(5, "When problems occur, I look for causes and start with what I can solve.");
        m.put(6, "Even when plans change or the future is uncertain, I try to cope rather than panic.");
        m.put(7, "I feel that I have people I can rely on when things are hard.");
        m.put(8, "In difficult periods, I tend to believe \"It will eventually be okay.\"");
        m.put(9, "I believe difficult experiences can still lead to learning and growth.");
        m.put(10, "When small things go well, I tend to feel appreciation and a sense of accomplishment.");
        m.put(11, "After setbacks, I usually regain momentum within a short time.");
        m.put(12, "Even when anxious, I can gather information and make difficult decisions.");
        m.put(13, "Under pressure, I try to pause, breathe, and respond calmly.");
        m.put(14, "Even when goals collapse, I tend to rebuild them and move forward.");
        m.put(15, "I feel I can ask people around me for support when needed.");
        m.put(16, "I feel I have past experiences of overcoming similar difficulties.");
        m.put(17, "When given a new environment or role, I adapt without taking too long.");
        m.put(18, "I often feel confident that I can get through challenges.");
        m.put(19, "Even in hardship, I try to find meaning and possible gains.");
        m.put(20, "Looking across life domains, I generally feel satisfied.");
        return m;
    }

    private static Map<String, String> buildTciDetailItemsEn() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("NS_1", "I feel excited and expectant when trying new experiences.");
        m.put("NS_2", "I tend to try new things rather than stick only to familiar ones.");
        m.put("NS_3", "I feel life is more interesting when there are more changes.");
        m.put("NS_4", "I like meeting new people and visiting new places.");
        m.put("NS_5", "I prefer somewhat unpredictable situations over fixed routines.");
        m.put("NS_6", "I usually do not hesitate to learn or try new things.");
        m.put("NS_7", "I prefer varied experiences to repetitive daily routines.");
        m.put("NS_8", "I am curious and interested in new fields.");
        m.put("NS_9", "I tend to accept change rather than fear it.");
        m.put("NS_10", "I tend to adapt quickly to new environments.");

        m.put("HA_1", "I often think ahead about what could go wrong.");
        m.put("HA_2", "I feel anxious in uncertain situations.");
        m.put("HA_3", "I try to avoid situations that may be risky.");
        m.put("HA_4", "I often hesitate to take on challenges for fear of failure.");
        m.put("HA_5", "Because I worry a lot, I do not often feel fully at ease.");
        m.put("HA_6", "I think for a long time before making decisions.");
        m.put("HA_7", "I feel anxious when starting something new.");
        m.put("HA_8", "I worry about being criticized or rejected.");
        m.put("HA_9", "I often think about whether I might get sick.");
        m.put("HA_10", "I am very careful to avoid mistakes.");

        m.put("RD_1", "Praise or recognition from others makes me feel good.");
        m.put("RD_2", "Maintaining warm relationships with people around me is important.");
        m.put("RD_3", "I feel hurt when I am rejected or ignored.");
        m.put("RD_4", "Others' attention and support are motivating to me.");
        m.put("RD_5", "I often feel more satisfied being with others than being alone.");
        m.put("RD_6", "Conversations with close people often comfort me.");
        m.put("RD_7", "I tend to be mindful of others' reactions.");
        m.put("RD_8", "I have groups where I feel a sense of belonging.");
        m.put("RD_9", "I do not find it very difficult to ask for help.");
        m.put("RD_10", "How others view me matters to me.");

        m.put("P_1", "I do not easily give up on goals even when difficulties arise.");
        m.put("P_2", "I tend to see difficult tasks through to the end.");
        m.put("P_3", "I set long-term plans and follow them steadily.");
        m.put("P_4", "Even after setbacks, I tend to try again.");
        m.put("P_5", "I show persistence in tasks that require sustained effort.");
        m.put("P_6", "Once a goal is set, I keep working until I reach it.");
        m.put("P_7", "I keep going with difficult assignments rather than quitting.");
        m.put("P_8", "After failure, I identify causes and challenge again.");
        m.put("P_9", "I choose long-term goals over short-term rewards.");
        m.put("P_10", "I tend to complete tasks without delaying too much.");

        m.put("SD_1", "I tend to make decisions and act on my own.");
        m.put("SD_2", "I am relatively clear about what my goals are.");
        m.put("SD_3", "I try to carry out responsibilities on my own.");
        m.put("SD_4", "After failure, I look for improvements rather than only blaming myself.");
        m.put("SD_5", "I generally have good self-control.");
        m.put("SD_6", "I judge based on my own standards rather than only others' views.");
        m.put("SD_7", "I decide for myself what I want to do and then carry it out.");
        m.put("SD_8", "When I make mistakes, I try to correct them rather than make excuses.");
        m.put("SD_9", "I tend to manage my schedule and plans on my own.");
        m.put("SD_10", "I have a relatively good understanding of myself.");

        m.put("C_1", "I try to understand other people's perspectives.");
        m.put("C_2", "I tend to be considerate in tasks requiring collaboration.");
        m.put("C_3", "Even in conflict, I try to resolve issues through conversation.");
        m.put("C_4", "I like helping other people.");
        m.put("C_5", "I value fairness and sharing.");
        m.put("C_6", "I try to fulfill my role in a team.");
        m.put("C_7", "I listen carefully to other people's opinions.");
        m.put("C_8", "I prefer achieving good outcomes together over simply winning.");
        m.put("C_9", "I find it hard to ignore injustice.");
        m.put("C_10", "I tend to be considerate toward people in vulnerable situations.");

        m.put("ST_1", "I have felt deeply moved through nature or art.");
        m.put("ST_2", "I sometimes think about life meaning or something greater than myself.");
        m.put("ST_3", "My own beliefs and values influence how I live.");
        m.put("ST_4", "I sometimes feel a sense of wonder in everyday life.");
        m.put("ST_5", "I tend to be open to spiritual or transcendent experiences.");
        m.put("ST_6", "I am moved by artworks or natural scenery.");
        m.put("ST_7", "I sometimes think about my life's purpose.");
        m.put("ST_8", "I have my own philosophy or set of values.");
        m.put("ST_9", "I tend to find meaning in small things.");
        m.put("ST_10", "I value inner fulfillment more than material gain.");
        return m;
    }

    private static Map<String, String> buildNeoDetailItemsEn() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("N_1", "I often feel worry or anxiety.");
        m.put("N_2", "I am easily shaken when under stress.");
        m.put("N_3", "There are times when I feel depressed.");
        m.put("N_4", "I get irritated easily over small matters.");
        m.put("N_5", "My emotions tend to fluctuate.");
        m.put("N_6", "When I feel anxious, my sleep is easily disturbed.");
        m.put("N_7", "I tend to be sensitive to criticism.");
        m.put("N_8", "I often feel weighed down by worry.");
        m.put("N_9", "I find it hard to change my mood once it drops.");
        m.put("N_10", "I worry about being at a disadvantage.");

        m.put("E_1", "I feel comfortable in gatherings with many people.");
        m.put("E_2", "I tend to initiate conversations first.");
        m.put("E_3", "I am generally energetic.");
        m.put("E_4", "I feel good when I am with other people.");
        m.put("E_5", "I am active and sociable.");
        m.put("E_6", "I tend to lead conversations.");
        m.put("E_7", "I prefer being with others to being alone.");
        m.put("E_8", "I become comfortable with unfamiliar people relatively easily.");
        m.put("E_9", "I actively participate in interesting activities.");
        m.put("E_10", "I am comfortable speaking in front of people.");

        m.put("O_1", "I am highly interested in new ideas and experiences.");
        m.put("O_2", "I tend to have rich imagination.");
        m.put("O_3", "I enjoy appreciating art and nature.");
        m.put("O_4", "I try to think from various perspectives.");
        m.put("O_5", "I am curious and willing to learn.");
        m.put("O_6", "I am interested in abstract or philosophical topics.");
        m.put("O_7", "I enjoy trying new foods and cultures.");
        m.put("O_8", "I enjoy creative activities and expression.");
        m.put("O_9", "I am open to experiences different from daily routine.");
        m.put("O_10", "I sometimes think about ideals and values.");

        m.put("A_1", "I tend to trust other people easily.");
        m.put("A_2", "I like cooperating with others.");
        m.put("A_3", "I try to understand others' perspectives.");
        m.put("A_4", "I prefer harmony over conflict.");
        m.put("A_5", "I am generally considerate.");
        m.put("A_6", "I tend to accept others rather than blame them quickly.");
        m.put("A_7", "I feel happy about other people's success.");
        m.put("A_8", "I try to act honestly and fairly.");
        m.put("A_9", "I choose cooperation over competition.");
        m.put("A_10", "I tend to notice and understand others' emotions.");

        m.put("C_1", "I tend not to postpone tasks I need to do.");
        m.put("C_2", "I usually make plans and execute them.");
        m.put("C_3", "I am detail-oriented and responsible.");
        m.put("C_4", "I work steadily toward goals.");
        m.put("C_5", "I generally follow rules well.");
        m.put("C_6", "I finish tasks to completion.");
        m.put("C_7", "I use my time in a planned way.");
        m.put("C_8", "I pay attention to details.");
        m.put("C_9", "I keep putting effort into achieving goals.");
        m.put("C_10", "I tend to keep promises.");
        return m;
    }

    private LocalizationTexts() {}
}
