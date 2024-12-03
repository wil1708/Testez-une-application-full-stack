describe('Admin Login and Display Sessions', () => {
  it('Login successful and display sessions', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyTmFtZSIsImlhdCI6MTczMjYxODEwNCwiZXhwIjoxNzMyNzA0NTA0fQ.x2dj9HfaUoy5i7nvJQrm6kf6k8XMYxm8nr_hMbKeWFpaS0CriI5WJ-Mt6MpzD7XkfxK46fCmZQagAbXdneYc5g",
        type: "Bearer",
        id: 1,
        username: "yoga@studio.com",
        firstName: "Admin",
        lastName: "Admin",
        admin: true
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

    // Set up du intercept pour la liste de teachers avant le click sur Create
    cy.intercept('GET', '/api/teacher', {
      body: [
        {
          id: 1,
          lastName: "DELAHAYE",
          firstName: "Margot",
          createdAt: null,
          updatedAt: null
        },
        {
          id: 2,
          lastName: "THIERCELIN",
          firstName: "Hélène",
          createdAt: null,
          updatedAt: null
        }
      ]
    }).as('teachersList')

    // Initialisation du login et remplissage des fields
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`test!1234{enter}{enter}`)
    cy.wait('@login').its('response.statusCode').should('eq', 200)
    cy.url().should('include', '/sessions')
    cy.wait('@sessionsList')
    cy.contains('Yoga session soir').should('be.visible')

    // Click du bouton Create
    cy.contains('Create').click()
    cy.wait('@teachersList')

    //Remplissage du formulaire pour créer une nouvelle session
    cy.get('input[formControlName=name]').type('Morning Yoga')
    cy.get('input[formControlName=date]').type('2024-12-01')
    cy.get('textarea[formControlName=description]').type('A refreshing morning yoga session.')
    cy.get('mat-select[formControlName=teacher_id]').click()
    cy.get('mat-option').contains('Margot DELAHAYE').click()

    // Set up du intercept pour la création de la nouvelle sion avant le click sur Save
    cy.intercept('POST', '/api/session', {
      body: {
        id: 3,
        name: 'Morning Yoga',
        date: '2024-12-01T00:00:00.000+00:00',
        teacher_id: 1,
        description: 'session yoga relaxante du matin',
        users: [],
        createdAt: '2024-12-02T15:25:59',
        updatedAt: '2024-12-02T15:25:59'
      }
    }).as('createSession')

    // Set up du intercept de la liste de sessions après la création de la nouvelle session
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
          updatedAt: '2024-11-30T21:28:26'
        },
        {
          id: 3,
          name: 'Morning Yoga',
          date: '2024-12-01T00:00:00.000+00:00',
          teacher_id: 1,
          description: 'session yoga relaxante du matin',
          users: [],
          createdAt: '2024-12-02T15:25:59',
          updatedAt: '2024-12-02T15:25:59'
        }
      ]
    }).as('updatedSessionsList')

    // Click du bouton Save
    cy.get('button[type=submit]').contains('Save').click()

    // Vérification que la requête de création de session est réussie
    cy.wait('@createSession').its('response.statusCode').should('eq', 200)

    cy.wait('@updatedSessionsList')

    // Vérification que les deux sessions sont affichées dans la liste
    cy.contains('Yoga session soir').should('be.visible')
    cy.contains('Morning Yoga').should('be.visible')

    // Set up de l'intercept du détail de la session Morning Yoga
    cy.intercept('GET', '/api/session/3', {
      body: {
        id: 3,
        name: 'Morning Yoga',
        date: '2024-12-01T00:00:00.000+00:00',
        teacher_id: 1,
        description: 'session yoga relaxante du matin',
        users: [],
        createdAt: '2024-12-02T15:25:59',
        updatedAt: '2024-12-02T17:59:54'
      }
    }).as('sessionDetails')

    // Set up de l'intercept de la liste des teachers pour le click du bouton Edit
    cy.intercept('GET', '/api/teacher', {
      body: [
        {
          id: 1,
          lastName: "DELAHAYE",
          firstName: "Margot",
          createdAt: null,
          updatedAt: null
        },
        {
          id: 2,
          lastName: "THIERCELIN",
          firstName: "Hélène",
          createdAt: null,
          updatedAt: null
        }
      ]
    }).as('editTeachersList')

    // Click du bouton Edit pour la session Morning Yoga
    cy.contains('Morning Yoga').parents('.item').within(() => {
      cy.get('button').contains('Edit').click()
    })

    cy.wait('@sessionDetails')
    cy.wait('@editTeachersList')

    // S'assurer que les fields du formulaire sont affichés
    cy.get('input[formControlName=name]').should('exist')
    cy.get('input[formControlName=date]').should('exist')
    cy.get('textarea[formControlName=description]').should('exist')
    cy.get('mat-select[formControlName=teacher_id]').should('exist')

    // Remplissage du formulaire pour modifier la session
    cy.get('input[formControlName=name]').clear().type('Evening Yoga')

    // Set up de l'intercept pour la requête d'update de la session avec ID = 3
    cy.intercept('PUT', '/api/session/3', (req) => {
      req.reply({
        statusCode: 200,
        body: {
          id: 3,
          name: 'Evening Yoga',
          date: '2024-12-01T00:00:00.000+00:00',
          teacher_id: 1,
          description: 'session yoga relaxante du matin',
          users: [],
          createdAt: null,
          updatedAt: '2024-12-02T19:21:11.3703326'
        }
      })
    }).as('updateSession')

    // Set up de l'intercept de la liste de sessions après modification
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
          updatedAt: '2024-11-30T21:28:26'
        },
        {
          id: 3,
          name: 'Evening Yoga',
          date: '2024-12-01T00:00:00.000+00:00',
          teacher_id: 1,
          description: 'session yoga relaxante du matin',
          users: [],
          createdAt: null,
          updatedAt: '2024-12-02T19:21:11.3703326'
        }
      ]
    }).as('finalSessionsList')

    cy.get('button[type=submit]').contains('Save').click()

    // Vérification du résultat de la requête d'update par le code status =  200
    cy.wait('@updateSession').its('response.statusCode').should('eq', 200)

    cy.wait('@finalSessionsList')

    // Vérification que les deux sessions sont affichées avec le nom modifié
    cy.contains('Yoga session soir').should('be.visible')
    cy.contains('Evening Yoga').should('be.visible')

    // Click du bouton Logout pour déconnecter l'admin
    cy.contains('Logout').click()

    // Vérification que l'utilisateur est redirigé vers la home page
    cy.url().should('eq', Cypress.config().baseUrl)

    // S'assurer que les liens Login et Register sont visibles après Logout
    cy.contains('Login').should('be.visible')
    cy.contains('Register').should('be.visible')

  })
})

