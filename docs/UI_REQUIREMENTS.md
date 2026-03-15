# 4. UI Requirements

> **[← Back to Main](../REQUIREMENTS.md)**

---

## 4.0 Frontend Technology Stack

| Component | Technology | Version | Purpose |
|-----------|-----------|---------|---------|
| **Framework** | React.js | 18.0+ | UI library for building components |
| **Language** | JavaScript/TypeScript | ES2020+ | Frontend programming language |
| **Build Tool** | Vite or Create React App | Latest | Project bundler and dev server |
| **Package Manager** | npm | 8.0+ | Dependency management |
| **Routing** | React Router | 6.0+ | Single-page application routing |
| **HTTP Client** | Axios | 1.4+ | API communication |
| **State Management** | React Context/Hooks | Native | State management solution |
| **Styling** | CSS/SCSS or Tailwind CSS | Latest | Styling framework |
| **UI Components** | Material-UI or Bootstrap | Latest | Reusable component library |
| **Form Validation** | React Hook Form | Latest | Form state management |
| **Testing** | Jest + React Testing Library | Latest | Unit and component testing |

---

## 4.0a React Project Structure

**Vite + React 18 Project**

```
frontend/ (Vite or Create React App)
├── package.json                          # npm dependencies and scripts
├── vite.config.js or react-scripts       # Build configuration
├── src/
│   ├── index.jsx                         # React entry point
│   ├── App.jsx                           # Root component
│   ├── App.css                           # Global styles
│   ├── components/
│   │   ├── common/                       # Reusable components
│   │   │   ├── Navbar.jsx
│   │   │   ├── Footer.jsx
│   │   │   ├── Sidebar.jsx
│   │   │   ├── Button.jsx
│   │   │   └── Card.jsx
│   │   ├── auth/                         # Authentication components
│   │   │   ├── Login.jsx
│   │   │   ├── Register.jsx
│   │   │   └── ProtectedRoute.jsx
│   │   ├── student/                      # Student feature components
│   │   │   ├── Dashboard.jsx
│   │   │   ├── BrowseEvents.jsx
│   │   │   ├── EventDetails.jsx
│   │   │   ├── MyRegistrations.jsx
│   │   │   └── Profile.jsx
│   │   └── admin/                        # Admin feature components
│   │       ├── AdminDashboard.jsx
│   │       ├── CreateEvent.jsx
│   │       ├── ManageEvents.jsx
│   │       ├── ManageUsers.jsx
│   │       └── EventRegistrations.jsx
│   ├── pages/                            # Page-level components
│   │   ├── HomePage.jsx
│   │   ├── DashboardPage.jsx
│   │   ├── AdminPage.jsx
│   │   └── NotFoundPage.jsx
│   ├── services/                         # API service layer
│   │   ├── api.js                        # Axios instance with interceptors
│   │   ├── authService.js                # Authentication API calls
│   │   ├── eventService.js               # Event API calls
│   │   ├── registrationService.js        # Registration API calls
│   │   └── userService.js                # User API calls
│   ├── hooks/                            # Custom React hooks
│   │   ├── useAuth.js                    # Authentication hook
│   │   ├── useFetch.js                   # Data fetching hook
│   │   └── useDebounce.js                # Debounce hook
│   ├── context/                          # React Context for state
│   │   ├── AuthContext.js                # Authentication state
│   │   └── NotificationContext.js        # Notifications state
│   ├── utils/                            # Utility functions
│   │   ├── constants.js                  # App constants
│   │   ├── validation.js                 # Form validation
│   │   ├── formatters.js                 # Date/number formatting
│   │   └── localStorage.js               # Local storage helpers
│   ├── styles/                           # Global styles and variables
│   │   ├── index.css
│   │   ├── variables.css
│   │   └── responsive.css
│   └── assets/                           # Static assets
│       ├── images/
│       ├── icons/
│       └── fonts/
├── public/                               # Static files
│   ├── index.html
│   └── favicon.ico
├── .env.example                          # Environment variables template
├── README.md
└── .gitignore
```

### React Development Best Practices

- **Functional Components**: Use React hooks (useState, useEffect, useContext)
- **Component Organization**: Group related components in feature folders
- **API Service Layer**: Centralized API calls using Axios with interceptors
- **Context API**: For authentication and global state management
- **React Router**: Use <Routes> and <Route> for SPA navigation
- **Form Handling**: React Hook Form for form state and validation
- **Environment Variables**: Use .env files for configuration
- **Error Boundaries**: Handle component errors gracefully
- **Code Splitting**: Lazy load routes using React.lazy()

---

## 4.1 Design System

| Usage             | Color     | Hex Code | RGB            |
|-------------------|-----------|----------|----------------|
| Primary           | Blue      | #0066CC  | rgb(0, 102, 204) |
| Secondary         | Teal      | #00A896  | rgb(0, 168, 150) |
| Success           | Green     | #28A745  | rgb(40, 167, 69) |
| Warning           | Orange    | #FF9800  | rgb(255, 152, 0) |
| Danger            | Red       | #DC3545  | rgb(220, 53, 69) |
| Background Light  | Off-white | #F5F5F5  | rgb(245, 245, 245) |
| Background Dark   | Dark Gray | #1F1F1F  | rgb(31, 31, 31) |
| Text Primary      | Dark Gray | #333333  | rgb(51, 51, 51) |
| Text Secondary    | Gray      | #666666  | rgb(102, 102, 102) |
| Border            | Light Gray| #CCCCCC  | rgb(204, 204, 204) |

### 4.1.2 Typography

| Element        | Font Family   | Size | Weight | Line Height |
|----------------|---------------|------|--------|-------------|
| Heading 1      | Segoe UI      | 32px | 700    | 1.2         |
| Heading 2      | Segoe UI      | 24px | 600    | 1.3         |
| Heading 3      | Segoe UI      | 20px | 600    | 1.3         |
| Heading 4      | Segoe UI      | 16px | 600    | 1.4         |
| Body Text      | Segoe UI      | 14px | 400    | 1.5         |
| Small Text     | Segoe UI      | 12px | 400    | 1.4         |
| Button Text    | Segoe UI      | 14px | 500    | 1.4         |

### 4.1.3 Spacing

- **8px grid system** - All spacing follows multiples of 8px
  - xs: 4px (half unit)
  - sm: 8px
  - md: 16px
  - lg: 24px
  - xl: 32px
  - 2xl: 48px
  - 3xl: 64px

### 4.1.4 Shadows & Depth

| Level | Elevation | Box Shadow |
|-------|-----------|------------|
| 1     | Raised    | 0 2px 4px rgba(0,0,0,0.1) |
| 2     | Floating  | 0 4px 8px rgba(0,0,0,0.15) |
| 3     | Modal     | 0 8px 16px rgba(0,0,0,0.2) |

### 4.1.5 Border Radius

- Button: 4px
- Card: 8px
- Modal: 12px
- Avatar: 50% (circle)

---

## 4.2 Screen Layouts

### 4.2.1 Student Screens

#### Home / Dashboard
- Welcome banner with user name
- Quick stats: Registered events, Upcoming events
- "Browse Events" CTA button
- Recent registrations list
- Upcoming events widget (next 7 days)

#### Browse Events
- Header: Page title + Search bar
- Sidebar (left, collapsible on mobile):
  - Category filter (checkboxes)
  - Date range picker
  - Organizer search
  - Status filter
- Main content (right):
  - Event cards grid (2 columns on desktop, 1 on mobile)
  - Each card shows: title, date, location, capacity, register button
  - Pagination at bottom
  - "No results" message if empty

#### Event Details
- Hero image (if available)
- Event title and status badge
- Key details grid: Date | Time | Location | Category
- Full description
- Organizer info card
- Capacity bar (visual progress)
- Registration button (or "Already Registered" / "Event Full" message)
- Related events section

#### My Registrations
- Tabs: All | Upcoming | Past | Cancelled
- List view with registration cards:
  - Event title, date, location
  - Registration date
  - Status badge
  - Actions: View Details, Cancel Registration
- No registrations message: "You haven't registered for any events yet."

#### Profile
- Tabs: Personal Info | Password | Activity
- Personal Info Tab:
  - Display name, email, phone, department
  - Edit button launches modal form
  - Save/Cancel buttons
- Password Tab:
  - Change password form with current password validation
- Activity Tab:
  - Login history table
  - Last login timestamp

### 4.2.2 Admin Screens

#### Admin Dashboard
- Top stats cards: Total Students | Total Events | Total Registrations | Pending Approvals
- Charts section:
  - Events created trend (bar chart)
  - Top 5 events by registrations (pie chart)
- Recent activity log (table)
- Quick action buttons: Create Event, View Users, View Registrations

#### Create Event
- Form layout:
  - Left: Form fields (Title, Category, Description, etc.)
  - Right: Preview card (live update as user types)
- Required field indicators
- Form validation with error messages
- Submit button + Cancel button
- Success modal after creation with options: Edit | Create Another | Go to Dashboard

#### Manage Events
- Table view with columns: Title | Category | Date | Status | Registrations | Actions
- Action buttons:
  - View: Opens event details modal
  - Edit: Opens edit form
  - Approve/Reject: Status change options
  - Delete: With confirmation
- Filters: Status (All | Pending | Approved | Cancelled)
- Search by title
- Bulk actions checkbox

#### View Registrations (for specific event)
- Event header with title and details
- Registrations table:
  - Student Name | Email | Registration Date | Status | Actions
- Status options: Pending | Attended | Cancelled | Removed
- Bulk mark as attended
- Export button (CSV/PDF)
- Search by student name
- Filter by status

#### Manage Users
- Table view: Name | Email | Registration Date | Total Events | Status | Actions
- Action buttons:
  - View Profile: Opens detailed profile modal
  - Disable/Enable: Account status toggle
- Search by name or email
- Sort by: Name | Registration Date | Events Count

---

## 4.3 Component Specifications

### 4.3.1 Buttons

**Primary Button**
- Background: #0066CC (Primary Blue)
- Text: White, 14px, Bold
- Padding: 12px 24px
- Border Radius: 4px
- Hover: Darken background to #0052A3
- Active: Further darkened
- Disabled: Gray (#CCCCCC) with opacity 0.6

**Secondary Button**
- Background: Transparent
- Text: #0066CC
- Border: 2px solid #0066CC
- Padding: 10px 22px (accounts for border)
- Hover: Light blue background (#E6F0FF)

**Danger Button**
- Background: #DC3545 (Red)
- Same styling as primary but with danger color

**Button States**
- Normal, Hover, Active, Disabled, Loading (with spinner)

### 4.3.2 Forms & Input Fields

**Text Input**
- Border: 1px solid #CCCCCC
- Padding: 10px 12px
- Font: 14px
- Border Radius: 4px
- Focus: Blue border (#0066CC), box shadow
- Error: Red border (#DC3545), red text for error message
- Placeholder: Gray text (#999999)
- Height: 40px (standard)

**Select Dropdown**
- Same styling as text input
- Dropdown arrow on right side
- Options list max height: 300px (scrollable)

**Checkbox & Radio**
- Size: 18px
- Color: #0066CC when checked
- Label font: 14px, clickable

**Textarea**
- Same styling as text input but multiline
- Min height: 120px
- Resizable

**Date Picker**
- Inline calendar widget
- Month/year navigation
- Today highlight
- Range selection support

### 4.3.3 Cards

- Background: White
- Border: 1px solid #E0E0E0
- Border Radius: 8px
- Padding: 16px
- Box Shadow: Level 1
- Hover: Box Shadow Level 2 (on clickable cards)

### 4.3.4 Tables

- Header background: #F5F5F5
- Header text: Bold, 14px
- Row height: 48px
- Alternating row colors: White and #FAFAFA
- Borders: 1px solid #E0E0E0
- Hover row: Light blue background (#F0F7FF)
- Sticky header for scrollable tables

### 4.3.5 Modal Dialogs

- Overlay: Semi-transparent black (rgba(0,0,0,0.5))
- Modal background: White
- Border Radius: 12px
- Box Shadow: Level 3
- Header: 24px heading + close button (X)
- Body: 16px padding
- Footer: Action buttons right-aligned
- Escape key to close
- Click outside to close (optional based on importance)

### 4.3.6 Toast Notifications

- Position: Top-right corner
- Width: 400px (max)
- Padding: 16px
- Border Radius: 8px
- Auto-dismiss: 5 seconds (or manual close)
- Success: Green (#28A745) background with white text
- Error: Red (#DC3545) background with white text
- Info: Blue (#0066CC) background with white text
- Warning: Orange (#FF9800) background with white text

### 4.3.7 Loading States

- **Spinner**: Rotating circle, 40px size, #0066CC color
- **Skeleton Screen**: Gray placeholder blocks (#E0E0E0) matching content shape
- **Button Loading**: Spinner inside button + disabled state

### 4.3.8 Badges

- Status badges:
  - PENDING: Orange/Yellow background (#FF9800)
  - APPROVED: Green background (#28A745)
  - REJECTED: Red background (#DC3545)
  - CANCELLED: Gray background (#999999)
- Padding: 4px 8px
- Border Radius: 12px
- Font size: 12px, bold
- Color: White text

---

## 4.4 User Flows

### 4.4.1 Student Registration Flow
1. User clicks "Sign Up" on landing page
2. Registration form displayed
3. User enters email, name, password
4. Password strength indicator shows real-time feedback
5. User submits form
6. Validation on client and server
7. Success message or error displayed
8. Redirect to login page on success

### 4.4.2 Student Login Flow
1. User enters email and password
2. Submit button triggers API call
3. Loading state shown
4. On success: JWT token stored, redirect to dashboard
5. On error: Error message displayed, password field cleared

### 4.4.3 Event Registration Flow
1. Student on browse events page finds an event
2. Clicks event card to view details
3. Clicks "Register" button
4. Confirmation dialog appears
5. User confirms
6. Loading state
7. Success notification
8. "Register" button changes to "Registered" state
9. Capacity updated

### 4.4.4 Admin Event Creation Flow
1. Admin clicks "Create Event" button
2. Form displayed with preview on right
3. Admin fills in event details
4. Real-time preview updates
5. Admin clicks "Create"
6. Validation performed
7. Success modal with next action options
8. Event created in PENDING status

---

## 4.5 Accessibility Requirements

- WCAG 2.1 AA compliance
- Color contrast ratio: Minimum 4.5:1 for text
- All images have alt text
- Form inputs have associated labels
- Keyboard navigation support (Tab, Enter, Escape)
- Focus indicators visible
- Screen reader support
- Error messages linked to form fields
- Skip to main content link

---

## 4.6 Responsive Design Breakpoints

| Breakpoint | Width    | Device Type     |
|-----------|----------|-----------------|
| xs        | < 480px  | Small phones    |
| sm        | 480px    | Mobile phones   |
| md        | 768px    | Tablets         |
| lg        | 1024px   | Laptops         |
| xl        | 1440px   | Desktops        |
| 2xl       | 1920px   | Large displays  |

### Responsive Behavior

- **Mobile (< 768px)**: Single column, hamburger menu, large touch targets (48px min)
- **Tablet (768px - 1024px)**: Two column layout, collapsible sidebar
- **Desktop (> 1024px)**: Full layout with persistent sidebar, optimized for mouse

