# Environment Setup Guide

This project uses environment variables for configuration. Follow these steps to set up your environment:

## JWT Configuration

The JWT authentication system requires secret keys that should not be committed to version control. These keys are stored in a `.env` file.

### Setting up your environment

1. Copy the example environment file:
   ```bash
   cp .env.example .env
   ```

2. Edit the `.env` file and set your secret keys:
   ```
   # JWT Configuration
   JWT_SECRET_KEY=your_secure_jwt_secret_key_here
   JWT_REFRESH_SECRET_KEY=your_secure_jwt_refresh_key_here
   
   # Token Validity (in milliseconds)
   # Default: 24 hours for access token, 7 days for refresh token
   JWT_ACCESS_TOKEN_VALIDITY=86400000
   JWT_REFRESH_TOKEN_VALIDITY=604800000
   ```

   > **Important**: Generate strong, unique secret keys. Do not use the default values.

3. Make sure the `.env` file is in your `.gitignore` to prevent it from being committed.

### Generating Secure Keys

You can generate secure keys using the following methods:

#### Option 1: Using OpenSSL
```bash
openssl rand -base64 32
```

#### Option 2: Using Java
```java
import java.security.SecureRandom;
import java.util.Base64;

public class GenerateSecureKey {
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // 256 bits
        random.nextBytes(bytes);
        String key = Base64.getEncoder().encodeToString(bytes);
        System.out.println(key);
    }
}
```

## Environment Variables

| Variable | Description | Default Value |
|----------|-------------|---------------|
| `JWT_SECRET_KEY` | Secret key for signing access tokens | None (must be set) |
| `JWT_REFRESH_SECRET_KEY` | Secret key for signing refresh tokens | None (must be set) |
| `JWT_ACCESS_TOKEN_VALIDITY` | Validity period for access tokens in milliseconds | 86400000 (24 hours) |
| `JWT_REFRESH_TOKEN_VALIDITY` | Validity period for refresh tokens in milliseconds | 604800000 (7 days) |