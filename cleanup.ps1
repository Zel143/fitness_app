# FitTrack Cleanup Script
# Removes unnecessary development, testing, and documentation files

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "  FitTrack App Cleanup Script" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""

# Function to safely delete a file
function Remove-FileIfExists {
    param (
        [string]$FilePath,
        [string]$Description
    )
    
    if (Test-Path $FilePath) {
        Remove-Item $FilePath -Force
        Write-Host "✓ Deleted: $Description" -ForegroundColor Green
        return $true
    } else {
        Write-Host "○ Not found: $Description" -ForegroundColor Yellow
        return $false
    }
}

# Ask user which cleanup level they want
Write-Host "Choose cleanup level:" -ForegroundColor Cyan
Write-Host "1. Minimal (Delete 6 files - test scripts, MySQL files, build artifacts)"
Write-Host "2. Full (Delete 16 files - minimal + old documentation)"
Write-Host "3. Custom (Choose which files to delete)"
Write-Host ""
$choice = Read-Host "Enter your choice (1/2/3)"

$deletedCount = 0

if ($choice -eq "1" -or $choice -eq "2") {
    Write-Host ""
    Write-Host "=========================================" -ForegroundColor Cyan
    Write-Host "  MINIMAL CLEANUP" -ForegroundColor Cyan
    Write-Host "=========================================" -ForegroundColor Cyan
    Write-Host ""
    
    # Delete test/utility scripts
    Write-Host "Removing test/utility scripts..." -ForegroundColor Yellow
    if (Remove-FileIfExists "src\main\java\com\fittrack\CheckUsers.java" "CheckUsers.java") { $deletedCount++ }
    if (Remove-FileIfExists "src\main\java\com\fittrack\TestConnection.java" "TestConnection.java") { $deletedCount++ }
    if (Remove-FileIfExists "src\main\java\com\fittrack\VerifyData.java" "VerifyData.java") { $deletedCount++ }
    
    Write-Host ""
    Write-Host "Removing MySQL-specific files..." -ForegroundColor Yellow
    if (Remove-FileIfExists "fix_food_log_table.sql" "fix_food_log_table.sql") { $deletedCount++ }
    
    Write-Host ""
    Write-Host "Removing Maven build artifacts..." -ForegroundColor Yellow
    if (Remove-FileIfExists "dependency-reduced-pom.xml" "dependency-reduced-pom.xml") { $deletedCount++ }
    
    Write-Host ""
    Write-Host "Removing empty directories..." -ForegroundColor Yellow
    if (Test-Path "FitTrack") {
        if ((Get-ChildItem "FitTrack" -Force | Measure-Object).Count -eq 0) {
            Remove-Item "FitTrack" -Force -Recurse
            Write-Host "✓ Deleted: Empty FitTrack folder" -ForegroundColor Green
            $deletedCount++
        } else {
            Write-Host "○ Skipped: FitTrack folder (not empty)" -ForegroundColor Yellow
        }
    } else {
        Write-Host "○ Not found: FitTrack folder" -ForegroundColor Yellow
    }
}

if ($choice -eq "2") {
    Write-Host ""
    Write-Host "=========================================" -ForegroundColor Cyan
    Write-Host "  FULL CLEANUP (Additional Files)" -ForegroundColor Cyan
    Write-Host "=========================================" -ForegroundColor Cyan
    Write-Host ""
    
    Write-Host "Removing old documentation files..." -ForegroundColor Yellow
    if (Remove-FileIfExists "DATABASE_MIGRATION_SUMMARY.md" "DATABASE_MIGRATION_SUMMARY.md") { $deletedCount++ }
    if (Remove-FileIfExists "DATABASE_SAVING_FIXED.md" "DATABASE_SAVING_FIXED.md") { $deletedCount++ }
    if (Remove-FileIfExists "DATA_PERSISTENCE_FIXED.md" "DATA_PERSISTENCE_FIXED.md") { $deletedCount++ }
    if (Remove-FileIfExists "SQLITE_MIGRATION.md" "SQLITE_MIGRATION.md") { $deletedCount++ }
    if (Remove-FileIfExists "NEW_FEATURES_SUMMARY.md" "NEW_FEATURES_SUMMARY.md") { $deletedCount++ }
    if (Remove-FileIfExists "OPTIMIZATION_SUMMARY.md" "OPTIMIZATION_SUMMARY.md") { $deletedCount++ }
    if (Remove-FileIfExists "PROJECT_SUMMARY.md" "PROJECT_SUMMARY.md") { $deletedCount++ }
    if (Remove-FileIfExists "QUICKSTART.md" "QUICKSTART.md") { $deletedCount++ }
    if (Remove-FileIfExists "TODO.md" "TODO.md") { $deletedCount++ }
}

if ($choice -eq "3") {
    Write-Host ""
    Write-Host "=========================================" -ForegroundColor Cyan
    Write-Host "  CUSTOM CLEANUP" -ForegroundColor Cyan
    Write-Host "=========================================" -ForegroundColor Cyan
    Write-Host ""
    
    $files = @(
        @{Path="src\main\java\com\fittrack\CheckUsers.java"; Desc="CheckUsers.java (test script)"},
        @{Path="src\main\java\com\fittrack\TestConnection.java"; Desc="TestConnection.java (test script)"},
        @{Path="src\main\java\com\fittrack\VerifyData.java"; Desc="VerifyData.java (test script)"},
        @{Path="fix_food_log_table.sql"; Desc="fix_food_log_table.sql (MySQL script)"},
        @{Path="dependency-reduced-pom.xml"; Desc="dependency-reduced-pom.xml (Maven artifact)"},
        @{Path="DATABASE_MIGRATION_SUMMARY.md"; Desc="DATABASE_MIGRATION_SUMMARY.md (old docs)"},
        @{Path="DATABASE_SAVING_FIXED.md"; Desc="DATABASE_SAVING_FIXED.md (old docs)"},
        @{Path="DATA_PERSISTENCE_FIXED.md"; Desc="DATA_PERSISTENCE_FIXED.md (old docs)"},
        @{Path="SQLITE_MIGRATION.md"; Desc="SQLITE_MIGRATION.md (old docs)"},
        @{Path="NEW_FEATURES_SUMMARY.md"; Desc="NEW_FEATURES_SUMMARY.md (old docs)"},
        @{Path="OPTIMIZATION_SUMMARY.md"; Desc="OPTIMIZATION_SUMMARY.md (old docs)"},
        @{Path="PROJECT_SUMMARY.md"; Desc="PROJECT_SUMMARY.md (old docs)"},
        @{Path="QUICKSTART.md"; Desc="QUICKSTART.md (old docs)"},
        @{Path="TODO.md"; Desc="TODO.md (old docs)"}
    )
    
    foreach ($file in $files) {
        if (Test-Path $file.Path) {
            $answer = Read-Host "Delete $($file.Desc)? (y/n)"
            if ($answer -eq "y" -or $answer -eq "Y") {
                Remove-Item $file.Path -Force
                Write-Host "✓ Deleted: $($file.Desc)" -ForegroundColor Green
                $deletedCount++
            } else {
                Write-Host "○ Skipped: $($file.Desc)" -ForegroundColor Yellow
            }
        } else {
            Write-Host "○ Not found: $($file.Desc)" -ForegroundColor Yellow
        }
    }
    
    # Check empty FitTrack folder
    if (Test-Path "FitTrack") {
        if ((Get-ChildItem "FitTrack" -Force | Measure-Object).Count -eq 0) {
            $answer = Read-Host "Delete empty FitTrack folder? (y/n)"
            if ($answer -eq "y" -or $answer -eq "Y") {
                Remove-Item "FitTrack" -Force -Recurse
                Write-Host "✓ Deleted: Empty FitTrack folder" -ForegroundColor Green
                $deletedCount++
            }
        }
    }
}

# Summary
Write-Host ""
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "  CLEANUP COMPLETE" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Total files deleted: $deletedCount" -ForegroundColor Green
Write-Host ""

# List remaining essential files
Write-Host "✅ ESSENTIAL FILES KEPT:" -ForegroundColor Green
Write-Host "   • fittrack.db (Your database - IMPORTANT!)" -ForegroundColor White
Write-Host "   • pom.xml (Maven configuration)" -ForegroundColor White
Write-Host "   • All Java source files in src/" -ForegroundColor White
Write-Host "   • All FXML view files" -ForegroundColor White
Write-Host "   • .gitignore" -ForegroundColor White
Write-Host ""

# List recommended documentation to keep
if ($choice -eq "1" -or $choice -eq "2") {
    Write-Host "📚 DOCUMENTATION KEPT:" -ForegroundColor Cyan
    if (Test-Path "README.md") { Write-Host "   • README.md" -ForegroundColor White }
    if (Test-Path "SETUP_FOR_GROUPMATES.md") { Write-Host "   • SETUP_FOR_GROUPMATES.md" -ForegroundColor White }
    if (Test-Path "TESTING_CHECKLIST.md") { Write-Host "   • TESTING_CHECKLIST.md" -ForegroundColor White }
    if (Test-Path "LOGIC_ERRORS_ANALYSIS.md") { Write-Host "   • LOGIC_ERRORS_ANALYSIS.md" -ForegroundColor White }
    Write-Host ""
}

Write-Host "Your FitTrack app is now cleaned up and ready!" -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Yellow
Write-Host "1. Run: mvn clean compile" -ForegroundColor White
Write-Host "2. Test: mvn javafx:run" -ForegroundColor White
Write-Host "3. Commit changes to Git" -ForegroundColor White
Write-Host ""

Read-Host "Press Enter to exit"
