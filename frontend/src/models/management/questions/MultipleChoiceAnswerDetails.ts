import Option from '@/models/management/Option';
import AnswerDetails from '@/models/management/questions/AnswerDetails';
import { QuestionTypes, convertToLetter } from '@/services/QuestionHelpers';

export default class MultipleChoiceAnswerType extends AnswerDetails {
  option: Option[] = [];

  constructor(jsonObj?: MultipleChoiceAnswerType) {
    super(QuestionTypes.MultipleChoice);
    if (jsonObj) {
      this.option = jsonObj.option.map((option: Option) => new Option(option));
    }
  }

  isCorrect(): boolean {
    return this.option.length == this.option.filter((x) => x.correct).length;
  }
  answerRepresentation(): string {
    return (
      this.option
        .map((x) => '' + (convertToLetter(x.sequence) || 0))
        .join(' | ') || '-'
    );
  }
}
