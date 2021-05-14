describe('Manage Open Answer Quiz Walk-through', () => {
  before(() => {
    cy.resetDatabase();
    cy.cleanOpenAnswerQuestionsByName('Cypress Question Example');
  });
  after(() => {
    cy.resetDatabase();
    cy.cleanOpenAnswerQuestionsByName('Cypress Question Example');
  });

  beforeEach(() => {
    cy.demoTeacherLogin();
    cy.get('[data-cy="managementMenuButton"]').click();
    cy.get('[data-cy="questionsTeacherMenuButton"]').click();

  });

  afterEach(() => {
    cy.logout();
  });

  it('create, solve, and show quiz with open answer question', function() {
    // create quizz
    cy.get('button').contains('New Question').click();

    cy.get('[data-cy="createOrEditQuestionDialog"]')
      .parent()
      .should('be.visible');

    cy.get(
      '[data-cy="questionTitleTextArea"]'
    ).type('Cypress Question Example - 01', { force: true });
    cy.get('[data-cy="questionQuestionTextArea"]').type(
      'Cypress Question Example - Content - 01',
      {
        force: true,
      }
    );

    cy.get('[data-cy="questionTypeInput"]')
      .type('open_answer', { force: true })
      .click({ force: true });

    cy.wait(1000);

    cy.get('[data-cy="questionSuggestionTextArea"]').type('Cypress Suggestion Example', { force: true });


    cy.get('button').contains('Save').click();


    cy.get('[data-cy="managementMenuButton"]').click();
    cy.get('[data-cy="quizzesTeacherMenuButton"]').click();

    cy.get('[data-cy="newQuizButton"]').click();
    cy.get('[data-cy="quizTitleTextArea"]').type('Cypress Open Answer Quiz');

    cy.get('#availableDateInput-input').click();
    cy.get(
      '.datetimepicker > .datepicker > .datepicker-buttons-container > .datepicker-button > .datepicker-button-content'
    ).click();

    cy.contains('Cypress Question Example - 01')
      .parent()
      .should('have.length', 1)
      .parent()
      .children()
      .should('have.length', 5)
      .find('[data-cy="addToQuizButton"]')
      .click();

    cy.get('[data-cy="saveQuizButton"]').click();

    cy.logout();

    cy.demoStudentLogin();

    // student answer quizz
    cy.get('[data-cy="quizzesStudentMenuButton"]').click();
    cy.contains('Available').click();

    cy.contains('Cypress Open Answer Quiz').click();

    cy.get('[data-cy="questionSuggestionTextArea"]').type('Cypress Suggestion Example', { force: true });

    cy.get('[data-cy="endQuizButton"]').click();
    cy.get('[data-cy="confirmationButton"]').click();


    // student see results
    cy.get('[data-cy="quizzesStudentMenuButton"]').click();
    cy.contains('Solved').click();

    cy.contains('Cypress Open Answer Quiz').click();

    cy.get('[data-cy="quizAnswerArea"]').should('contain', 'Cypress Suggestion Example');
  });
})