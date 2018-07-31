package org.mapfish.print.map.geotools.function;

import com.google.common.collect.Lists;
import org.geotools.filter.FunctionExpressionImpl;
import org.opengis.feature.type.Name;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A factory for building all the mapfish print functions.
 */
public final class FunctionFactory implements org.geotools.filter.FunctionFactory {
    private List<? extends FunctionExpressionImpl> functions = Lists.newArrayList(
            new MultiplicationFunction()
    );

    @Override
    public List<FunctionName> getFunctionNames() {
        return this.functions.stream().map(FunctionExpressionImpl::getFunctionName)
                .collect(Collectors.toList());
    }

    @Override
    public Function function(final String name, final List<Expression> args, final Literal fallback) {
        for (FunctionExpressionImpl template: this.functions) {
            if (template.getName().equals(name)) {
                try {
                    final FunctionExpressionImpl function = template.getClass().newInstance();
                    function.setParameters(args);
                    return function;
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    @Override
    public Function function(final Name name, final List<Expression> args, final Literal fallback) {
        return function(name.getLocalPart(), args, fallback);
    }
}
