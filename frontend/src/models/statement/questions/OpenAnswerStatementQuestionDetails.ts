import StatementQuestionDetails from '@/models/statement/questions/StatementQuestionDetails';
import { QuestionTypes } from '@/services/QuestionHelpers';
import { _ } from 'vue-underscore';
import StatementOption from '@/models/statement/StatementOption';

export default class OpenAnswerStatementQuestionDetails extends StatementQuestionDetails {
  answer: string = '';

  constructor(jsonObj?: OpenAnswerStatementQuestionDetails) {
    super(QuestionTypes.OpenAnswer);

    if (jsonObj) {
      this.answer = jsonObj.answer;
    }
  }
}
