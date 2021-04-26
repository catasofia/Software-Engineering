import Option from '@/models/management/Option';
import QuestionDetails from '@/models/management/questions/QuestionDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';

export default class MultipleChoiceQuestionDetails extends QuestionDetails {
  options: Option[] = [new Option(), new Option(), new Option(), new Option()];

  constructor(jsonObj?: MultipleChoiceQuestionDetails) {
    super(QuestionTypes.MultipleChoice);
    if (jsonObj) {
      this.options = jsonObj.options.map(
        (option: Option) => new Option(option)
      );
    }
    this.options.sort((a, b) => {
      if (a.id == null) a.id = -1;
      if (b.id == null) b.id = -1;
      return a.id - b.id;
    });
  }

  setAsNew(): void {
    this.options.forEach((option) => {
      option.id = null;
    });
  }
}
