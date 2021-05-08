package pt.ulisboa.tecnico.socialsoftware.tutor.answer.dto;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.MultipleChoiceAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.OptionDto;

import java.util.List;
import java.util.stream.Collectors;

public class MultipleChoiceAnswerDto extends AnswerDetailsDto {
    private List<OptionDto> option;

    public MultipleChoiceAnswerDto() {
    }

    public MultipleChoiceAnswerDto(MultipleChoiceAnswer answer) {
        if (answer.getOption() != null)
            this.option = answer.getOption().stream().map(OptionDto::new).collect(Collectors.toList());
    }

    public List<OptionDto> getOption() {
        return option;
    }

    public void setOption(List<OptionDto> option) {
        this.option = option;
    }
}
