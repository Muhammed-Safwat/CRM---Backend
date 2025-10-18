# User and Employee Export Usage

## Overview
The `UserEmployeeExportHandler` has been created to export user and employee data to Excel format, similar to the existing LeadExportHandler.

## How to Use

### 1. API Request
Send a POST request to `/api/exports` with the following JSON:

```json
{
  "ids": [1, 2, 3, 4, 5],
  "type": "excel",
  "referenceType": "User"
}
```

### 2. What Gets Exported
The export creates an Excel file with two sheets:

#### Users Sheet (👥 Users)
- 🆔 ID
- 👤 Name  
- 📧 Username
- 📞 Phone
- 📅 Created At
- 🔒 Enabled
- 🔓 Locked
- 🗑️ Deleted

#### Employees Sheet (👨‍💼 Employees)
- 🆔 ID
- 👤 Name
- 📧 Username
- 💼 Job Name
- 📞 Phone
- 📅 Created At
- 🔒 Enabled
- 👥 Privilege Group
- 👨‍💼 Admin

### 3. Features
- ✅ Modern styling with emojis and colors
- ✅ Auto-sized columns
- ✅ Professional formatting
- ✅ Handles null values gracefully (shows "N/A")
- ✅ Two separate sheets for Users and Employees
- ✅ Summary information at the top of each sheet

### 4. File Naming
- Default filename: `users_employees_export.xlsx`
- Generated filename: `{short_uuid}.xlsx`

## Example Response
After successful export, you'll get a response with:
- Task ID
- File path
- File size
- Status (QUEUED → PROCESSING → COMPLETED)

## Notes
- The handler supports both User and Employee entities
- All data is fetched by the provided IDs
- The export is processed asynchronously
- Files are stored in the configured storage service
