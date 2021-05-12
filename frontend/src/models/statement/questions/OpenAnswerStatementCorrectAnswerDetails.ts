import StatementCorrectAnswerDetails from '@/models/statement/questions/StatementCorrectAnswerDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';

export default class OpenAnswerStatementCorrectAnswerDetails extends StatementCorrectAnswerDetails {
  public answer!: string;

  constructor(jsonObj?: OpenAnswerStatementCorrectAnswerDetails) {
    super(QuestionTypes.OpenAnswer);
    if (jsonObj) {
      this.answer = jsonObj.answer;
    }
  }
}
