package Inference;

public enum InferenceProduct {
    TRUE("Prawda"),
    FALSE("Fałsz"),
    DONT_KNOW("Nie udowodniono");

    private String msg;

    InferenceProduct(String s)
    {
        msg = s;
    }

    @Override
    public String toString() {
        return msg;
    }
}
