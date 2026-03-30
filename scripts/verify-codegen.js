#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

console.log('🔍 Verifying codegen setup...');

// Check if spec file exists
const specPath = path.join(__dirname, '..', 'src', 'NativeSensitiveInfo.ts');
if (fs.existsSync(specPath)) {
  console.log('✅ Spec file exists:', specPath);
  const content = fs.readFileSync(specPath, 'utf8');
  if (content.includes('TurboModule')) {
    console.log('✅ Spec file contains TurboModule interface');
  } else {
    console.log('❌ Spec file missing TurboModule interface');
  }
} else {
  console.log('❌ Spec file not found:', specPath);
}

// Check package.json codegen config
const packagePath = path.join(__dirname, '..', 'package.json');
const packageJson = JSON.parse(fs.readFileSync(packagePath, 'utf8'));
if (packageJson.codegenConfig) {
  console.log('✅ Codegen config found in package.json');
  console.log('   Name:', packageJson.codegenConfig.name);
  console.log('   Type:', packageJson.codegenConfig.type);
  console.log('   Source dir:', packageJson.codegenConfig.jsSrcsDir);
  if (packageJson.codegenConfig.android?.javaPackageName) {
    console.log(
      '   Android package:',
      packageJson.codegenConfig.android.javaPackageName
    );
  }
  if (packageJson.codegenConfig.ios?.modulesProvider) {
    console.log(
      '   iOS modulesProvider:',
      JSON.stringify(packageJson.codegenConfig.ios.modulesProvider)
    );
  }
} else {
  console.log('❌ No codegen config in package.json');
}

console.log('\n📋 To enable new architecture in your app:');
console.log('1. iOS: Set RCT_NEW_ARCH_ENABLED=1 in Podfile');
console.log('2. Android: Set newArchEnabled=true in gradle.properties');
console.log('3. Run: cd ios && pod install');
console.log('4. Clean and rebuild your project');
