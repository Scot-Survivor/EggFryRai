package com.comp5590.utils;

public class StringUtils {

    // generate random string beteween 2 lengths (inclusive)
    public static String randomString(int minLength, int maxLength) {
        int length = NumberUtils.randomInt(minLength, maxLength);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) NumberUtils.randomInt(97, 122));
        }
        return sb.toString();
    }

    // generate random password with all types of characters
    public static String randomPassword(int minLength, int maxLength) {
        int length = NumberUtils.randomInt(minLength, maxLength);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int type = NumberUtils.randomInt(0, 3);
            switch (type) {
                case 0:
                    sb.append((char) NumberUtils.randomInt(97, 122));
                    break;
                case 1:
                    sb.append((char) NumberUtils.randomInt(65, 90));
                    break;
                case 2:
                    sb.append((char) NumberUtils.randomInt(48, 57));
                    break;
                case 3:
                    sb.append((char) NumberUtils.randomInt(33, 47));
                    break;
            }
        }
        return sb.toString();
    }

    // generate gibberish health conditions / notes / diagnosis / prescription /
    // patient info, using an array of a ton of it
    public static String randomHealthCondition(int howMany) {
        String[] healthConditions = {
            "Aneurysm",
            "Asthma",
            "Bronchitis",
            "Cancer",
            "Celiac Disease",
            "Chronic Fatigue Syndrome",
            "Chronic Obstructive Pulmonary Disease",
            "Cirrhosis",
            "Crohn's Disease",
            "Cystic Fibrosis",
            "Diabetes",
            "Epilepsy",
            "Fibromyalgia",
            "Gallstones",
            "Gout",
            "Heart Disease",
            "Hemorrhoids",
            "Hepatitis",
            "Hernia",
            "Hypertension",
            "Irritable Bowel Syndrome",
            "Kidney Stones",
            "Lupus",
            "Lyme Disease",
            "Migraine",
            "Multiple Sclerosis",
            "Obesity",
            "Osteoporosis",
            "Parkinson's Disease",
            "Peptic Ulcer",
            "Pneumonia",
            "Psoriasis",
            "Rheumatoid Arthritis",
            "Scoliosis",
            "Sinusitis",
            "Sleep Apnea",
            "Stroke",
            "Thyroid Disease",
            "Ulcerative Colitis",
            "Varicose Veins",
            "Acne",
            "Allergies",
            "Anemia",
            "Anxiety",
            "Arthritis",
            "Atrial Fibrillation",
            "Back Pain",
            "Bipolar Disorder",
            "Carpal Tunnel Syndrome",
            "Celiac Disease",
            "Chronic Pain",
            "Depression",
            "Eczema",
            "Endometriosis",
            "Fibromyalgia",
            "Gastroesophageal Reflux Disease",
            "Headache",
            "Hemorrhoids",
            "Hypothyroidism",
            "Insomnia",
            "Irritable Bowel Syndrome",
            "Menopause",
            "Migraine",
            "Osteoarthritis",
            "Osteoporosis",
            "Restless Legs Syndrome",
            "Rheumatoid Arthritis",
            "Sciatica",
            "Sinusitis",
            "Sleep Apnea",
            "Tinnitus",
            "Ulcerative Colitis",
            "Urinary Incontinence",
            "Vaginal Yeast Infection",
            "Vertigo",
            "Acne",
            "Allergies",
            "Anemia",
            "Anxiety",
            "Arthritis",
            "Asthma",
            "Back Pain",
            "Bipolar Disorder",
            "Carpal Tunnel Syndrome",
            "Celiac Disease",
            "Chronic Pain",
            "Depression",
            "Eczema",
            "Endometriosis",
            "Fibromyalgia",
            "Gastroesophageal Reflux Disease",
            "Headache",
            "Hemorrhoids",
            "Hypothyroidism",
            "Insomnia",
            "Irritable Bowel Syndrome",
            "Menopause",
            "Migraine",
            "Osteoarthritis",
            "Osteoporosis",
            "Restless Legs Syndrome",
            "Rheumatoid Arthritis",
            "Sciatica",
            "Sinusitis",
            "Sleep Apnea",
            "Tinnitus",
            "Ulcerative Colitis",
            "Urinary Incontinence",
            "Vaginal Yeast Infection",
            "Vertigo",
            "Acne",
            "Allergies",
            "Anemia",
            "Anxiety",
            "Arthritis",
            "Asthma",
            "Back Pain",
            "Bipolar Disorder",
            "Carpal Tunnel Syndrome",
            "Celiac Disease",
            "Chronic Pain",
            "Depression",
            "Eczema",
            "Endometriosis",
            "Fibromyalgia",
            "Gastroesophageal Reflux Disease",
            "Headache",
            "Hemorrhoids",
            "Hypothyroidism",
            "Insomnia",
            "Irritable Bowel Syndrome",
            "Menopause",
            "Migraine",
            "Osteoarthritis",
            "Osteoporosis",
            "Restless Legs Syndrome",
            "Rheumatoid Arthritis",
            "Sciatica",
            "Sinusitis",
            "Sleep Apnea",
            "Tinnitus",
            "Ulcerative Colitis",
            "Urinary Incontinence",
            "Vaginal Yeast Infection",
            "Vertigo",
            "Acne",
            "Allergies",
            "Anemia",
            "Anxiety",
            "Arthritis",
            "Asthma",
            "Back Pain",
            "Bipolar Disorder",
            "Carpal Tunnel Syndrome",
            "Celiac Disease",
            "Chronic Pain",
            "Depression",
            "Eczema",
            "Endometriosis",
            "Fibromyalgia",
            "Gastroesophageal Reflux Disease",
            "Headache",
            "Hemorrhoids",
            "Hypothyroidism",
            "Insomnia",
            "Irritable Bowel Syndrome",
            "Menopause",
            "Migraine",
            "Osteoarthritis",
            "Osteoporosis",
            "Restless Legs Syndrome",
            "Rheumatoid Arthritis",
            "Sciatica",
            "Sinusitis",
            "Sleep Apnea",
            "Tinnitus",
            "Ulcerative Colitis",
            "Urinary Incontinence",
            "Vaginal Yeast Infection",
            "Vertigo",
        };

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= howMany; i++) {
            // if i is 4, don't add a comma
            if (i == howMany) {
                sb.append("and ");
                int index = NumberUtils.randomInt(0, healthConditions.length - 1);
                sb.append(healthConditions[index]);
                sb.append(".");
                break;
            }

            int index = NumberUtils.randomInt(0, healthConditions.length - 1);
            sb.append(healthConditions[index]);
            sb.append(", ");
        }

        return String.format("This patient has: %s", sb.toString());
    }
}
