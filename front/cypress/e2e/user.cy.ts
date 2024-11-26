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
  
    it('Login and navigate to session details', () => {
      cy.visit('/login')
  
      cy.intercept('POST', '/api/auth/login', {
        body: {
          token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3aWxsaWFtcGlyZXNAZXhhbXBsZS5jb20iLCJpYXQiOjE3MzI2MTgxMDQsImV4cCI6MTczMjcwNDUwNH0.x2dj9HfaUoy5i7nvJQrm6kf6k8XMYxm8nr_hMbKeWFpaS0CriI5WJ-Mt6MpzD7XkfxK46fCmZQagAbXdneYc5g",
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
  
      cy.get('input[formControlName=email]').type("williampires@example.com")
      cy.get('input[formControlName=password]').type(`password123{enter}{enter}`)
  
      cy.wait('@login').its('response.statusCode').should('eq', 200)
  
      cy.url().should('include', '/sessions')
  
      cy.wait('@sessionsList')
  
      // Verify the session element and the presence of the Detail button
      cy.contains('Yoga session soir').parents('mat-card').within(() => {
        cy.get('button').contains('Detail').should('be.visible')
      })
    })
  })
  