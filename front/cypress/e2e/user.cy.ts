describe('Register and Session Participation', () => {
  it('Register new user successfully', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {
      body: {
        id: 2,
        email: 'williampires@example.com',
        firstName: 'William',
        lastName: 'PIRES',
        admin: false,
        createdAt: new Date(),
        updatedAt: new Date()
      }
    }).as('register')

    cy.get('input[formControlName=firstName]').type("William")
    cy.get('input[formControlName=lastName]').type("PIRES")
    cy.get('input[formControlName=email]').type("williampires@example.com")
    cy.get('input[formControlName=password]').type("password123")

    cy.get('button[type=submit]').click()

    cy.wait('@register').its('response.statusCode').should('eq', 200)

    cy.url().should('include', '/login')
  })

  it('Login, navigate to session details, participate/unparticipate in the session, navigate to account page, and delete the account', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJIUzUxMiJ9.eyJzdWIiOiJ3aWxsaWFtcGlyZXNAZXhhbXBsZS5jb20iLCJpYXQiOjE3MzI2MTgxMDQsImV4cCI6MTczMjcwNDUwNH0.x2dj9HfaUoy5i7nvJQrm6kf6k8XMYxm8nr_hMbKeWFpaS0CriI5WJ-Mt6MpzD7XkfxK46fCmZQagAbXdneYc5g",
        type: "Bearer",
        id: 2,
        username: "williampires@example.com",
        firstName: "William",
        lastName: "PIRES",
        admin: false
      }
    }).as('login')

    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: 'Yoga session soir',
          date: '2024-11-26T00:00:00.000+00:00',
          teacher_id: 1,
          description: 'Session yoga relaxante du soir',
          users: [],
          createdAt: '2024-11-26T10:04:22',
          updatedAt: '2024-11-26T10:04:22'
        }
      ]
    }).as('sessionsList')

    cy.intercept('GET', '/api/teacher/1', {
      body: {
        id: 1,
        lastName: "DELAHAYE",
        firstName: "Margot",
        createdAt: null,
        updatedAt: null
      }
    }).as('teacherDetails')

    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'Yoga session soir',
        date: '2024-11-26T00:00:00.000+00:00',
        teacher_id: 1,
        description: 'Session yoga relaxante du soir',
        users: [],
        createdAt: '2024-11-26T10:04:22',
        updatedAt: '2024-11-26T10:04:22'
      }
    }).as('sessionDetails')

    cy.intercept('POST', '/api/session/1/participate/2', {
      statusCode: 200
    }).as('participate')

    cy.intercept('DELETE', '/api/session/1/participate/2', {
      statusCode: 200
    }).as('unparticipate')

    cy.intercept('GET', '/api/user/2', {
      body: {
        id: 2,
        email: 'williampires@example.com',
        firstName: 'William',
        lastName: 'PIRES',
        admin: false,
        createdAt: new Date('2024-11-01T00:00:00.000+00:00'),
        updatedAt: new Date('2024-11-30T00:00:00.000+00:00')
      }
    }).as('userDetails')

    cy.intercept('DELETE', '/api/user/2', {
      statusCode: 200
    }).as('deleteUser')

    cy.get('input[formControlName=email]').type("williampires@example.com")
    cy.get('input[formControlName=password]').type(`password123{enter}{enter}`)

    cy.wait('@login').its('response.statusCode').should('eq', 200)

    cy.url().should('include', '/sessions')

    cy.wait('@sessionsList')

    // Verify the session element and the presence of the Detail button, then click it
    cy.contains('Yoga session soir').parents('mat-card').within(() => {
      cy.get('button').contains('Detail').click()
    })

    cy.wait('@sessionDetails')
    cy.wait('@teacherDetails')

    // Verify navigation to the session details page and that the session details are loaded
    cy.url().should('include', '/sessions/detail/1')
    cy.contains('Margot DELAHAYE').should('be.visible')
    cy.contains('Session yoga relaxante du soir').should('be.visible')

    // Mocking the response for the session to reflect participation
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'Yoga session soir',
        date: '2024-11-26T00:00:00.000+00:00',
        teacher_id: 1,
        description: 'Session yoga relaxante du soir',
        users: [2],
        createdAt: '2024-11-26T10:04:22',
        updatedAt: '2024-11-30T20:23:26'
      }
    }).as('sessionDetailsAfterParticipation')

    // Click on Participate button
    cy.get('button').contains('Participate').click()
    cy.wait('@participate').its('response.statusCode').should('eq', 200)

    // Verify button changes to "Do not participate"
    cy.get('button').contains('Do not participate').should('be.visible')

    // Mocking the response for the session to reflect unparticipation
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'Yoga session soir',
        date: '2024-11-26T00:00:00.000+00:00',
        teacher_id: 1,
        description: 'Session yoga relaxante du soir',
        users: [],
        createdAt: '2024-11-26T10:04:22',
        updatedAt: '2024-11-30T20:23:26'
      }
    }).as('sessionDetailsAfterUnparticipation')

    // Click on "Do not participate" button
    cy.get('button').contains('Do not participate').click()
    cy.wait('@unparticipate').its('response.statusCode').should('eq', 200)

    // Verify button changes back to "Participate"
    cy.get('button').contains('Participate').should('be.visible')

    // Click on the back button to navigate to sessions list
    cy.get('button').contains('arrow_back').click()

    // Verify navigation back to the sessions list
    cy.url().should('include', '/sessions')

    // Click on the Account link to navigate to the account page
    cy.get('span').contains('Account').click()

    // Verify navigation to the account page
    cy.url().should('include', '/me')

    // Verify the user information on the account page
    cy.wait('@userDetails')
    cy.contains('Name: William PIRES').should('be.visible')
    cy.contains('Email: williampires@example.com').should('be.visible')
    cy.contains('Create at: November 1, 2024').should('be.visible')
    cy.contains('Last update: November 30, 2024').should('be.visible')

    // Click on the delete button to delete the account
    cy.get('button').contains('Detail').click()
    cy.wait('@deleteUser').its('response.statusCode').should('eq', 200)

    // Verify navigation to the home page after account deletion
    cy.url().should('eq', Cypress.config().baseUrl)

    
  })
})




