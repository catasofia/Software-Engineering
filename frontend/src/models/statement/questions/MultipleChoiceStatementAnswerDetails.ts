import StatementAnswerDetails from '@/models/statement/questions/StatementAnswerDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';
import MultipleChoiceStatementCorrectAnswerDetails from '@/models/statement/questions/MultipleChoiceStatementCorrectAnswerDetails';

export default class MultipleChoiceStatementAnswerDetails extends StatementAnswerDetails {
  public optionsId!: number[];

  constructor(jsonObj?: MultipleChoiceStatementAnswerDetails) {
    super(QuestionTypes.MultipleChoice);
    if (jsonObj) {
      this.optionsId = jsonObj.optionsId || [];
    }
  }

  isQuestionAnswered(): boolean {
    return this.optionsId != null;
  }

  isAnswerCorrect(
    correctAnswerDetails: MultipleChoiceStatementCorrectAnswerDetails
  ): boolean {
    if (!!correctAnswerDetails) {
      if (
        correctAnswerDetails.correctOptionsId.length == this.optionsId.length
      ) {
        for (let i = 0; i < correctAnswerDetails.correctOptionsId.length; ++i) {
          if (correctAnswerDetails.correctOptionsId[i] !== this.optionsId[i])
            return false;
        }
        return true;
      }
    }
    return false;
  }
}
