import org.mule.transformer.AbstractTransformer

class ToUpperTransformer extends AbstractTransformer {

    protected Object doTransform(Object o, String s) {
        return o.toLowerCase();
    }

}