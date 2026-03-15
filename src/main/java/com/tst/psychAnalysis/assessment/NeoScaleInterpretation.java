package com.tst.psychAnalysis.assessment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * NEO 성격검사 하위척도별 T점수 구간 해석.
 * 기준: 아주 낮음 ~34, 낮음 35~44, 평균 45~55, 높음 56~65, 매우 높음 66~ (참고: NEO 전문가 지침서·해석 블로그).
 */
public final class NeoScaleInterpretation {

    /** T점수 5구간 (블로그 기준): ~34 아주 낮음, 35~44 낮음, 45~55 평균, 56~65 높음, 66~ 매우 높음 */
    private static final double BAND_LOW_MAX = 44;
    private static final double BAND_AVG_MAX = 55;

    /** 하위척도 표시 순서: N1~N10, E1~E4, O1~O4, A1~A5, C1~C6 */
    public static final List<String> FACET_ORDER = List.of(
        "N1", "N2", "N3", "N4", "N5", "N6", "N7", "N8", "N9", "N10",
        "E1", "E2", "E3", "E4",
        "O1", "O2", "O3", "O4",
        "A1", "A2", "A3", "A4", "A5",
        "C1", "C2", "C3", "C4", "C5", "C6"
    );

    /** 기존 5요인 순서 (주척도만 쓸 때) */
    public static final List<String> SCALE_ORDER = List.of("N", "E", "O", "A", "C");

    /** 주척도 코드 → 한글명 (척도별 그룹 헤더용) */
    public static final Map<String, String> MAIN_FACTOR_NAMES = Map.of(
        "N", "신경증", "E", "외향성", "O", "개방성", "A", "친화성", "C", "성실성"
    );

    private static final Map<String, String> FACET_NAMES = buildFacetNames();
    private static final Map<String, Map<String, String>> FACET_TEXTS = buildFacetTexts();

    private static Map<String, String> buildFacetNames() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("N1", "불안"); m.put("N2", "적대감"); m.put("N3", "우울"); m.put("N4", "충동성"); m.put("N5", "사회적 위축");
        m.put("N6", "정서충격"); m.put("N7", "심약"); m.put("N8", "특이성"); m.put("N9", "반사회성"); m.put("N10", "자존감");
        m.put("E1", "사회성"); m.put("E2", "지배성"); m.put("E3", "자극추구"); m.put("E4", "활동성");
        m.put("O1", "창의성"); m.put("O2", "심미성"); m.put("O3", "정서성"); m.put("O4", "행동진취성");
        m.put("A1", "온정성"); m.put("A2", "신뢰성"); m.put("A3", "공감성"); m.put("A4", "관용성"); m.put("A5", "겸손성");
        m.put("C1", "유능감"); m.put("C2", "성취동기"); m.put("C3", "조직성"); m.put("C4", "책임감"); m.put("C5", "자제력"); m.put("C6", "신중성");
        return m;
    }

    private static Map<String, Map<String, String>> buildFacetTexts() {
        Map<String, Map<String, String>> out = new LinkedHashMap<>();
        // N
        put(out, "N1", "불안이 적고 차분한 편입니다.", "평균 범위입니다.", "불안하고 초조하며 긴장·두려움과 걱정이 많고, 극복할 수 있는 일도 미리 걱정하고 스트레스를 받는 편입니다.");
        put(out, "N2", "비판이나 좌절에 덜 반응하는 편입니다.", "평균 범위입니다.", "욕구 불만·좌절감을 많이 겪고, 사람에 실망하거나 화를 잘 내며 다른 사람의 비판에 매우 민감한 편입니다.");
        put(out, "N3", "무기력·낙심이 적은 편입니다.", "평균 범위입니다.", "매사에 무기력하고 일이 잘 안 되면 죄책감을 느끼며 쉽게 낙심·포기하고, 갑자기 슬퍼지고 눈물이 나는 편입니다.");
        put(out, "N4", "충동 조절이 잘 되는 편입니다.", "평균 범위입니다.", "성미가 급하고 자신을 통제하지 못하며, 생각 없이 말이나 행동이 먼저 나가 후회할 때가 많은 편입니다.");
        put(out, "N5", "사람들 앞에서 덜 위축되는 편입니다.", "평균 범위입니다.", "자신에 대한 확신이 부족하여 사람들 사이에서 놀림이나 조롱을 당할까 크게 두려워하는 편입니다.");
        put(out, "N6", "충격적 경험에 덜 휩쓸리는 편입니다.", "평균 범위입니다.", "충격적인 사건에 대한 심상과 기억이 반복적으로 떠올라 관련 상황에 불안을 느끼고 회피하려는 편입니다.");
        put(out, "N7", "스트레스에 잘 견디는 편입니다.", "평균 범위입니다.", "조그마한 스트레스에도 잘 견디지 못하고 절망·갈등을 느끼며, 자신을 도와줄 사람을 먼저 찾게 되는 편입니다.");
        put(out, "N8", "사람들과 잘 어울리고 사고가 체계적인 편입니다.", "평균 범위입니다.", "사람들의 행위와 감정에 무관심하고 어울리기보다 뒤로 물러서며, 사고 과정이 모호하고 체계적이지 않은 편입니다.");
        put(out, "N9", "규칙과 가치를 수용하는 편입니다.", "평균 범위입니다.", "법이나 규칙을 반하고 기존 사회적·정치적·종교적 가치를 인정하지 않으며, 부모나 교사의 지도에 비판적인 편입니다.");
        put(out, "N10", "자신감이 있고 열등감이 적은 편입니다.", "평균 범위입니다.", "자신감이 부족하고 열등감이 있으며, 기운이 없고 자신을 허약하게 생각하는 편입니다.");
        // E
        put(out, "E1", "사람들과의 교제보다 혼자가 편하고 주변 관계가 단조로운 편입니다.", "평균 범위입니다.", "사람들과 교제하고 만나는 것이 즐겁고, 처음 만난 사람과도 쉽게 친해지는 편입니다.");
        put(out, "E2", "소심하고 수줍음이 많으며 이야기를 들어주는 입장인 편입니다.", "평균 범위입니다.", "자기주장이 강하고 설득력 있으며, 사람들에게 말을 잘하는 지도자 역할을 하는 편입니다.");
        put(out, "E3", "조심스럽고 새로운 변화·모험을 피하며 단조로운 일도 편하게 수행하는 편입니다.", "평균 범위입니다.", "복잡하고 모험적인 환경을 선호하며, 거친 운동·자극적인 놀이를 즐기는 편입니다.");
        put(out, "E4", "과묵하고 신중하며 조심스럽고, 일의 속도는 느리나 꾸준히 완성하는 편입니다.", "평균 범위입니다.", "매사에 바쁘고 활동적이며 융통성이 있고 적극적이며 재빠른 편입니다.");
        // O
        put(out, "O1", "현실적이고 구체적이며 단조로움을 선호하고, 이전에 시도한 방식을 고수하는 편입니다.", "평균 범위입니다.", "공상을 즐기고 상상력·창의력이 풍부하며, 기발하고 독특한 의견이 많은 편입니다.");
        put(out, "O2", "감정보다는 사실·문제에 관심이 많고, 감정에 흔들리지 않고 일을 처리하는 편입니다.", "평균 범위입니다.", "순수 예술과 자연의 아름다움을 즐기고 추구하며, 경험을 중시하는 편입니다.");
        put(out, "O3", "지적인 호기심이 부족하고 논리적·합리적 사고를 잘 하지 못하는 편입니다.", "평균 범위입니다.", "섬세하고 깊은 감정을 풍부하게 느끼며, 자신과 타인의 감정을 중요하게 생각하는 편입니다.");
        put(out, "O4", "전통을 존중하며 변화를 싫어하고, 확실하고 일정한 일을 선호하는 편입니다.", "평균 범위입니다.", "새로운 행동을 시도하고 쉽게 적응하며, 다양한 취미 활동을 원하는 편입니다.");
        // A
        put(out, "A1", "사람들로부터 차갑다는 인상을 주고, 동정심보다 합리성에 기초하여 결정하는 편입니다.", "평균 범위입니다.", "자상하고 따뜻하며 친절한 관계를 유지하고, 타인을 적극적으로 돕고 싶어 하는 편입니다.");
        put(out, "A2", "사람은 악의를 갖고 접근한다고 생각하고 자기중심적·고집이 세운 편입니다.", "평균 범위입니다.", "사람은 선의를 가지고 접근한다고 생각하고 솔직하며, 타인에 대한 의심이 적은 편입니다.");
        put(out, "A3", "자기중심적이고 배타적이며, 타인의 문제에 개입하는 것을 피하는 편입니다.", "평균 범위입니다.", "타인의 행복에 적극적으로 관심을 보이고, 협동하고 헌신적이며 봉사적인 편입니다.");
        put(out, "A4", "협동보다 경쟁을 좋아하고 공격적이며, 상대의 잘못을 쉽게 잊지 않고 분노하는 편입니다.", "평균 범위입니다.", "사람들의 실수와 잘못을 잘 용서하고, 너그럽게 이해하며 화를 참는 편입니다.");
        put(out, "A5", "자신을 자랑하고 뽐내며, 다른 사람을 칭찬·인정하기 어려운 편입니다.", "평균 범위입니다.", "자신을 자랑하지 않고 뽐내지 않으며, 다른 사람을 칭찬하고 인정하는 겸손함이 있는 편입니다.");
        // C
        put(out, "C1", "스스로를 부족하다고 생각하고, 죄책감·열등감을 잘 느끼는 편입니다.", "평균 범위입니다.", "스스로를 능력 있고 효율적으로 생각하며 자신감이 넘치나, 자만할 수 있는 편입니다.");
        put(out, "C2", "감상적이고 게으를 수 있으며, 대체로 야망과 목적의식이 없는 편입니다.", "평균 범위입니다.", "성취 욕구가 강하고 포부 수준이 높으며, 늘 바르게 목표 달성을 위해 노력하는 편입니다.");
        put(out, "C3", "일상생활에서 정리 정돈이 어렵고, 생활 패턴이 불규칙한 편입니다.", "평균 범위입니다.", "깔끔하고 정리 정돈된 생활을 하고, 계획을 세우고 꼼꼼하게 지켜 나가는 편입니다.");
        put(out, "C4", "싫증 나면 지속하기 어렵고 쉽게 단념하며, 행동 기준에 무심하고 무신경한 편입니다.", "평균 범위입니다.", "자신의 윤리적 원칙을 고수하고, 끈기 있게 마무리하는 경향이 있는 편입니다.");
        put(out, "C5", "유혹에 잘 넘어가고 충동적인 행동을 하며, 목표를 향한 꾸준한 노력이 부족한 편입니다.", "평균 범위입니다.", "유혹에 잘 넘어가지 않고 충동적인 행동을 억제하며, 목표를 향해 꾸준히 노력하는 편입니다.");
        put(out, "C6", "결정을 성급히 내리고, 미래의 위험을 미리 예상·대비하기 어려운 편입니다.", "평균 범위입니다.", "결정을 내릴 때 신중하고 면밀하게 검토하며, 미래의 위험을 미리 예상하고 대비하는 편입니다.");
        // 주척도만 있을 때(기존 5요인) 해석용
        put(out, "N", "정서가 비교적 안정적인 편입니다. 스트레스나 좌절에 덜 흔들리며, 걱정이나 불안이 적은 경향이 있습니다.", "신경증 수준이 평균 범위입니다.", "정서가 예민하고 스트레스에 민감한 편입니다. 걱정, 불안, 기분 저하 등을 자주 느낄 수 있습니다.");
        put(out, "E", "내향적 성향이 있는 편입니다. 혼자 있는 시간을 편하게 느끼며, 소수와의 교류를 선호할 수 있습니다.", "외향성 수준이 평균 범위입니다.", "외향적이고 사교적인 편입니다. 사람들과 함께 있을 때 에너지를 얻으며, 적극적으로 소통하고 활동하는 경향이 있습니다.");
        put(out, "O", "현실적이고 익숙한 방식을 선호하는 편입니다. 새로운 아이디어보다는 구체적·실용적인 것에 익숙할 수 있습니다.", "개방성 수준이 평균 범위입니다.", "새로운 경험과 아이디어에 열려 있는 편입니다. 상상력과 호기심이 많고, 예술·자연·다양한 관점에 관심이 있습니다.");
        put(out, "A", "자기 주장이 강하거나 거리감이 있는 편일 수 있습니다. 타인과의 조화보다 원칙이나 효율을 우선할 수 있습니다.", "친화성 수준이 평균 범위입니다.", "따뜻하고 협력적인 편입니다. 타인의 입장을 이해하고 배려하며, 조화와 협력을 중요하게 여깁니다.");
        put(out, "C", "유연하고 즉흥적인 편일 수 있습니다. 계획보다는 상황에 맞춰 움직이거나, 규칙을 엄격히 지키기보다 여유 있게 대할 수 있습니다.", "성실성 수준이 평균 범위입니다.", "계획적이고 책임감이 있는 편입니다. 목표를 세우고 꾸준히 노력하며, 약속과 규칙을 지키려 합니다.");
        return out;
    }

    private static void put(Map<String, Map<String, String>> target, String facet, String low, String average, String high) {
        Map<String, String> band = new LinkedHashMap<>();
        band.put("low", low);
        band.put("average", average);
        band.put("high", high);
        target.put(facet, band);
    }

    /** 하위척도 T점수에 따른 5구간 해석 (아주 낮음/낮음 → low, 평균 → average, 높음/매우 높음 → high) */
    public static String interpret(String scaleCode, Double tScore) {
        if (tScore == null) return "";
        Map<String, String> band = FACET_TEXTS.get(scaleCode);
        if (band == null) return "";
        if (tScore <= BAND_LOW_MAX) return band.get("low");       // ~44
        if (tScore > BAND_AVG_MAX) return band.get("high");       // 56~
        return band.get("average");                               // 45~55
    }

    public static Map<String, String> interpretAll(Map<String, Double> scaleTScores) {
        Map<String, String> out = new LinkedHashMap<>();
        if (scaleTScores == null) return out;
        boolean useFacets = scaleTScores.keySet().stream().anyMatch(FACET_ORDER::contains);
        List<String> order = useFacets ? FACET_ORDER : SCALE_ORDER;
        for (String code : order) {
            if (!scaleTScores.containsKey(code)) continue;
            String text = interpret(code, scaleTScores.get(code));
            if (!text.isEmpty()) out.put(code, text);
        }
        return out;
    }

    public static String getFacetDisplayName(String code) {
        return FACET_NAMES.getOrDefault(code, code);
    }

    /** 관리자용: T점수 5구간 및 하위척도별 해석 기준 */
    public static String getReferenceDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("T점수 5구간(참고: NEO 전문가 지침서·해석): 아주 낮음 ~34, 낮음 35~44, 평균 45~55, 높음 56~65, 매우 높음 66~\n");
        sb.append("하위척도(29개): N1 불안 ~ N10 자존감, E1 사회성 ~ E4 활동성, O1 창의성 ~ O4 행동진취성, A1 온정성 ~ A5 겸손성, C1 유능감 ~ C6 신중성\n");
        for (String code : FACET_ORDER) {
            String name = FACET_NAMES.getOrDefault(code, code);
            Map<String, String> band = FACET_TEXTS.get(code);
            if (band == null) continue;
            sb.append("\n· ").append(code).append(" ").append(name).append("\n");
            sb.append("  낮음: ").append(band.get("low")).append("\n");
            sb.append("  평균: ").append(band.get("average")).append("\n");
            sb.append("  높음: ").append(band.get("high")).append("\n");
        }
        return sb.toString();
    }

    private NeoScaleInterpretation() {}
}
