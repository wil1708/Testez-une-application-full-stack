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
  
      cy.get('input[formControlName=email]').type("yoga@studio.com")
      cy.get('input[formControlName=password]').type(`test!1234{enter}{enter}`)
  
      cy.wait('@login').its('response.statusCode').should('eq', 200)
  
      cy.url().should('include', '/sessions')
  
      cy.wait('@sessionsList')
  
      // Verify the session is displayed
      cy.contains('Yoga session soir').should('be.visible')
  
    })
  })
  