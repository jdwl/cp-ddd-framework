package org.ddd.cp.ddd.runtime.registry.mock.step;

import lombok.extern.slf4j.Slf4j;
import org.ddd.cp.ddd.annotation.Step;
import org.ddd.cp.ddd.runtime.DDD;
import org.ddd.cp.ddd.runtime.registry.mock.ability.ReviseStepsAbility;
import org.ddd.cp.ddd.runtime.registry.mock.exception.FooException;
import org.ddd.cp.ddd.runtime.registry.mock.model.FooModel;

import javax.validation.constraints.NotNull;
import java.util.List;

@Step(groups = Steps.Submit.GoodsValidationGroup, dependsOn = FooStep.class)
@Slf4j
public class BarStep extends SubmitStep {

    @Override
    public void execute(@NotNull FooModel model) throws FooException {
        log.info("submit: {}", model);

        List<String> revisedSteps = DDD.findAbility(ReviseStepsAbility.class).revisedSteps(model);
        if (revisedSteps != null && !revisedSteps.isEmpty()) {
            log.info("重新编排步骤，增加步骤：{}", revisedSteps);

            // 通过异常，来改变后续步骤
            FooException ex = new FooException();
            ex.setSteps(revisedSteps);
            throw ex;
        }
    }

    @Override
    public String stepCode() {
        return Steps.Submit.BarStep;
    }
}
