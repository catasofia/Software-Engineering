describe('Manage Multiple Choice Quiz Walk-through', () => {
  
  after(() => {
    cy.resetDatabaseMultipleChoice();
  });

  beforeEach(() => {
    cy.demoTeacherLogin();
    cy.get('[data-cy="managementMenuButton"]').click();
    cy.get('[data-cy="questionsTeacherMenuButton"]').click();
  });

  afterEach(() => {
    cy.logout();
  });

  it('create, solve, and show quiz with multiple choice question', function () {

    cy.createQuestionWith2Correct('Question 1', 'Question 1 Content', 1, 2, 3, 4, 4, 2)


    cy.get('[data-cy="managementMenuButton"]').click();
    cy.get('[data-cy="quizzesTeacherMenuButton"]').click();

    cy.get('[data-cy="newQuizButton"]').click();
    cy.get('[data-cy="quizTitleTextArea"]').type('Cypress Multiple Choice Quiz');

    cy.get('#availableDateInput-input').click();
    cy.get(
      '.datetimepicker > .datepicker > .datepicker-buttons-container > .datepicker-button > .datepicker-button-content'
    ).click();

    cy.contains('Question 1')
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

    cy.get('[data-cy="quizzesStudentMenuButton"]').click();
    cy.contains('Available').click();

    cy.contains('Cypress Multiple Choice Quiz').click();

    cy.get('[data-cy="optionList"]').children().eq(1).click();
    cy.get('[data-cy="optionList"]').children().eq(3).click();

    cy.get('[data-cy="endQuizButton"]').click();
    cy.get('[data-cy="confirmationButton"]').click();


    cy.get('[data-cy="quizzesStudentMenuButton"]').click();
    cy.contains('Solved').click();

    cy.contains('Cypress Multiple Choice Quiz').click();

    cy.get('[data-cy="optionList"]')
      .should('be.visible')
  });
})