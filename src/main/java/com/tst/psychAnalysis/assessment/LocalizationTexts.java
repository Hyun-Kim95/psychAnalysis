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
    private static final Map<String, String> TCI_DETAIL_ITEMS_EN = buildTciDetailItemsEn();
    private static final Map<String, String> NEO_DETAIL_ITEMS_EN = buildNeoDetailItemsEn();
    private static final Map<String, String> NEO_SHORT_ITEMS_EN = buildNeoShortItemsEn();

    /** KRQ-53 한글 문항 순서와 동일 (Krq53MigrationRunner#getKrq53Texts) */
    private static final String[] KRQ53_ITEMS_EN = buildKrq53ItemsEn();

    /** 회복탄력성 검사(간단) 27문항 → KRQ53_ITEMS_EN 인덱스 (Krq53MigrationRunner와 동일) */
    private static final int[] KRQ_SHORT_TO_FULL_INDEX = {
            0, 1, 2, 6, 7, 8, 12, 13, 14, 18, 19, 20, 24, 25, 26, 30, 31, 32, 36, 37, 38, 42, 43, 44, 47, 48, 49
    };

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
            return NEO_SHORT_ITEMS_EN.getOrDefault(scaleCode + "_1", originalText);
        }
        if (assessmentName.equals("NEO 성격검사 (상세)")) {
            // DB: 하위척도 코드 N1..C6, 문항은 척도당 2개 → 키 N1_1, N1_2 (글로벌 item_number 아님)
            int withinFacet = ((itemNumber - 1) % 2) + 1;
            String neoKey = scaleCode + "_" + withinFacet;
            return NEO_DETAIL_ITEMS_EN.getOrDefault(neoKey, originalText);
        }
        if (assessmentName.contains("회복탄력성")) {
            return resilienceItemEnglish(assessmentName, itemNumber, originalText);
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

    /** NEO 간단(29문항): 하위척도당 1문항 → 상세 맵의 {@code Nx_1} 문장 재사용 */
    private static Map<String, String> buildNeoShortItemsEn() {
        Map<String, String> m = new LinkedHashMap<>();
        for (String facet : NeoScaleInterpretation.FACET_ORDER) {
            String key = facet + "_1";
            String v = NEO_DETAIL_ITEMS_EN.get(key);
            if (v != null) {
                m.put(key, v);
            }
        }
        return Map.copyOf(m);
    }

    private static String resilienceItemEnglish(String assessmentName, int itemNumber, String originalText) {
        boolean detailed = assessmentName.contains("상세");
        if (detailed) {
            if (itemNumber >= 1 && itemNumber <= KRQ53_ITEMS_EN.length) {
                return KRQ53_ITEMS_EN[itemNumber - 1];
            }
        } else {
            if (itemNumber >= 1 && itemNumber <= KRQ_SHORT_TO_FULL_INDEX.length) {
                return KRQ53_ITEMS_EN[KRQ_SHORT_TO_FULL_INDEX[itemNumber - 1]];
            }
        }
        return originalText;
    }

    private static String[] buildKrq53ItemsEn() {
        return new String[]{
                "When difficulties arise, I can control my emotions.",
                "When I have a thought, I notice well how it affects my mood.",
                "When debating important issues with family or friends, I can control my emotions well.",
                "When I have important work that requires focus, I feel more stressed than energized.",
                "I get swept up by my emotions.",
                "Sometimes emotional issues make it hard for me to concentrate at school or work.",
                "When I have an urgent task, I overcome temptations or distractions and get it done.",
                "No matter how confusing or difficult a situation is, I am usually aware of what I am thinking.",
                "When someone is angry at me, I first listen carefully to their point of view.",
                "When things do not go as planned, I tend to give up easily.",
                "I usually live without a clear plan for spending or expenses.",
                "I tend to handle things spontaneously rather than planning ahead.",
                "When a problem arises, I think about possible solutions first, then try to solve it.",
                "When a difficult situation occurs, I think carefully about the causes, then try to address it.",
                "In most situations, I believe I know the causes of problems well.",
                "I often hear that I do not grasp events or situations well.",
                "I often hear that I jump to conclusions when problems arise.",
                "When a difficult situation occurs, I think it is better to solve it quickly even if I have not fully understood the causes.",
                "Depending on the mood or my partner, I can guide conversations well.",
                "I am good at witty humor.",
                "I find appropriate words or phrases for what I want to say.",
                "Talking with people in authority feels burdensome to me.",
                "Sometimes I miss parts of a conversation because I am thinking of something else.",
                "Sometimes I hesitate and cannot say everything I want to say.",
                "Looking at people's facial expressions, I can tell what emotion they are feeling.",
                "When I see someone sad, angry, or flustered, I can tell pretty well what they are thinking.",
                "When a colleague is angry, I usually understand the reason fairly well.",
                "I sometimes find it hard to understand how people behave.",
                "I often hear from close friends, partners, or spouses that I do not understand them.",
                "Colleagues and friends say I do not listen well to what they say.",
                "I receive love and attention from people around me.",
                "I truly like my friends.",
                "People around me understand my feelings well.",
                "I have few friends with whom I help each other mutually.",
                "People I meet regularly mostly end up disliking me.",
                "I have almost no friends I can open up to honestly.",
                "I believe that if I work hard, there will always be rewards.",
                "Right or wrong, I think it is good to first believe that I can solve any difficult problem.",
                "Even in difficult situations, I am confident that everything will be resolved well.",
                "After I finish something, I worry that people around me may evaluate me negatively.",
                "I believe most problems I face are caused by situations beyond my control.",
                "When someone asks about my future, it is hard for me to imagine succeeding.",
                "My life is close to the ideal life I imagine.",
                "I am satisfied with various conditions in my life.",
                "I am satisfied with my life.",
                "I already have most of what I consider important in life.",
                "If I were born again, I would want to live my current life again.",
                "I feel grateful to many different kinds of people.",
                "If I wrote down everything I am grateful for, the list would be very long.",
                "As I age, I feel more gratitude for people, events, and experiences that are part of my life.",
                "I have little to be thankful for.",
                "When I look at the world, there is not much I feel grateful for.",
                "It takes me a long time before I feel gratitude toward people or things."
        };
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

    /**
     * NEO 상세(58문항): 하위척도 코드 N1..C6 × 척도당 2문항. 키는 {@code N1_1}, {@code N1_2} … (NeoMigrationRunner 순서).
     */
    private static Map<String, String> buildNeoDetailItemsEn() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("N1_1", "I often feel anxious, restless, tense, and preoccupied with fear or worry.");
        m.put("N1_2", "I tend to be someone who frequently feels anxious, tense, and worried.");
        m.put("N2_1", "I am sensitive to criticism and often feel frustration, resentment, or anger.");
        m.put("N2_2", "I tend to react with irritation or anger when I feel criticized or thwarted.");
        m.put("N3_1", "I often feel lethargic, guilty when things go wrong, easily discouraged, or suddenly sad.");
        m.put("N3_2", "I tend to lose motivation, feel hopeless, or sink into low moods.");
        m.put("N4_1", "I am quick-tempered, act before I think, and often regret impulsive words or actions.");
        m.put("N4_2", "I have trouble controlling impulses and later regret what I said or did.");
        m.put("N5_1", "I lack confidence in myself and fear being ridiculed or mocked in social situations.");
        m.put("N5_2", "I worry a lot about whether others look down on me in groups.");
        m.put("N6_1", "Intrusive memories or images of upsetting past events return and make me anxious.");
        m.put("N6_2", "I try to avoid reminders of frightening or shocking experiences I went through.");
        m.put("N7_1", "Even small stresses overwhelm me; I feel desperate, torn, and look for someone to help.");
        m.put("N7_2", "I do not cope well with pressure and quickly feel hopeless or trapped.");
        m.put("N8_1", "I keep emotional distance, stay in the background, and my thinking can feel vague or unusual.");
        m.put("N8_2", "I tend not to engage much with others' feelings or behavior and withdraw instead.");
        m.put("N9_1", "I resist rules or conventions and am critical of traditional social, political, or religious values.");
        m.put("N9_2", "I tend to be skeptical of authority and comfortable questioning established norms.");
        m.put("N10_1", "I lack confidence, often feel inferior, low in energy, and see myself as weak.");
        m.put("N10_2", "I tend to doubt my worth and view myself negatively.");

        m.put("E1_1", "I prefer being alone to socializing and my relationships feel monotonous.");
        m.put("E1_2", "I tend to be more comfortable alone and feel my social life is routine or flat.");
        m.put("E2_1", "I am assertive, persuasive, and often take a leadership role in conversations.");
        m.put("E2_2", "I tend to speak up confidently and influence others when we interact.");
        m.put("E3_1", "I prefer complex, adventurous environments and enjoy intense sports or stimulating play.");
        m.put("E3_2", "I tend to seek excitement, novelty, and physically or mentally stimulating activities.");
        m.put("E4_1", "I am busy, active, flexible, proactive, and quick to move.");
        m.put("E4_2", "I tend to keep a fast pace and stay on the go in daily life.");

        m.put("O1_1", "I enjoy fantasy, have rich imagination and creativity, and often have unusual ideas.");
        m.put("O1_2", "I tend to think in original, inventive ways and like imaginative exploration.");
        m.put("O2_1", "I appreciate pure art and natural beauty and value lived experience.");
        m.put("O2_2", "I tend to seek beauty in art and nature and care about aesthetic experience.");
        m.put("O3_1", "I feel emotions deeply and take my own and others' feelings seriously.");
        m.put("O3_2", "I am emotionally sensitive and attuned to subtle emotional nuances.");
        m.put("O4_1", "I try new behaviors, adapt easily, and want a variety of hobbies.");
        m.put("O4_2", "I tend to experiment, pick up new interests, and adapt quickly to change.");

        m.put("A1_1", "I am caring, warm, kind in relationships, and eager to help others actively.");
        m.put("A1_2", "I tend to nurture others and take their well-being to heart.");
        m.put("A2_1", "I believe people approach me in good faith; I am straightforward and seldom suspicious.");
        m.put("A2_2", "I tend to give others the benefit of the doubt and rarely distrust them.");
        m.put("A3_1", "I actively care about others' happiness, cooperate, am devoted, and act in a service-oriented way.");
        m.put("A3_2", "I tend to put energy into helping others succeed and feel fulfilled.");
        m.put("A4_1", "I forgive others' mistakes readily, understand generously, and hold back anger.");
        m.put("A4_2", "I tend to be lenient when people slip up and let go of resentment.");
        m.put("A5_1", "I do not boast or show off; I praise others and stay humble.");
        m.put("A5_2", "I tend to downplay my accomplishments and lift others up.");

        m.put("C1_1", "I see myself as capable and efficient, with strong self-confidence.");
        m.put("C1_2", "I tend to feel competent and effective in what I take on.");
        m.put("C2_1", "I have strong achievement motivation, high aspirations, and work steadily toward goals.");
        m.put("C2_2", "I tend to set ambitious goals and push myself to succeed in a disciplined way.");
        m.put("C3_1", "I keep my life tidy and organized, make plans, and follow through carefully.");
        m.put("C3_2", "I tend to rely on schedules, neatness, and careful follow-through.");
        m.put("C4_1", "I hold to my ethical principles and finish what I start with persistence.");
        m.put("C4_2", "I tend to honor commitments and stick with tasks until they are done.");
        m.put("C5_1", "I resist temptation, curb impulses, and work steadily toward long-term goals.");
        m.put("C5_2", "I tend to control immediate urges and stay focused on what matters.");
        m.put("C6_1", "I decide carefully after weighing details and anticipate future risks.");
        m.put("C6_2", "I tend to think things through thoroughly before acting and plan for pitfalls.");
        return Map.copyOf(m);
    }

    private LocalizationTexts() {}
}
