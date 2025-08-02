# PlaceLive-User-Service

👥 **User Identity & Social Relationship Management Service**

## Overview

**PlaceLive-User-Service** is the identity and social fabric engine of the PlaceLive ecosystem. This microservice manages user profiles, authentication, friend relationships, following/followers systems, and privacy controls. It serves as the foundation for all social interactions within PlaceLive, enabling secure user management while supporting the platform's revolutionary location-aware social features.

## 🚀 Key Features

### User Management
- **Secure Authentication**: JWT-based authentication with mobile/email verification
- **Profile Management**: Comprehensive user profiles with privacy controls
- **Multi-Factor Authentication**: Enhanced security with OTP verification
- **Account Recovery**: Secure password reset and account recovery flows
- **User Preferences**: Customizable settings for notifications, privacy, and behavior

### Social Relationship Engine
- **Friend System**: Send, accept, and manage friend requests
- **Follower/Following Model**: Asymmetric social connections for broader networks
- **Circle Management**: Organize friends into custom groups (family, colleagues, close friends)
- **Mutual Friends Discovery**: Find common connections between users
- **Social Graph Analytics**: Understand user connection patterns

### Privacy & Security
- **Granular Privacy Controls**: Fine-tuned visibility settings per relationship type
- **Place-Specific Permissions**: Different privacy rules for different location types
- **Role-Based Access**: Parent-child monitoring, workplace visibility controls
- **Data Protection**: GDPR-compliant user data management
- **Security Audit**: Complete activity logging and security monitoring

### Integration Hub
- **Geofencing Integration**: Provide user data for location-based features
- **Tracker Integration**: Supply friend relationships for presence visibility
- **Notification Integration**: Manage user communication preferences
- **Analytics Integration**: User behavior insights while preserving privacy

## 🏗️ Architecture

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Mobile Apps   │────│  API Gateway     │────│  User Service   │
│  Authentication │    │  (JWT Validation)│    │                 │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                                         │
                                                         ▼
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│  Notification   │────│   MySql          │────│  Common Library │
│   Service       │    │  (User Data)     │    │  (Generic CRUD) │
└─────────────────┘    └──────────────────┘    └─────────────────┘
```

### Technology Stack
- **Framework**: Spring Boot 2.x with Spring Security
- **Database**: PostgreSQL with optimized indexing
- **Authentication**: JWT with refresh tokens
- **Password Security**: BCrypt hashing with salt
- **Email Service**: Integration with SMTP providers
- **SMS Service**: OTP verification via SMS gateways
- **Caching**: Redis for session management

## 📊 Core Data Models

### User Entity
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    private String userId;
    
    @Column(unique = true)
    private String email;
    
    @Column(unique = true)
    private String phoneNumber;
    
    private String username;
    private String password; // BCrypt hashed
    private String firstName;
    private String lastName;
    private LocalDateTime dateOfBirth;
    private String profilePictureUrl;
    
    // Privacy settings
    private PrivacyLevel defaultPrivacyLevel;
    private boolean locationSharingEnabled;
    private boolean friendRequestsEnabled;
    
    // Account status
    private boolean emailVerified;
    private boolean phoneVerified;
    private boolean accountActive;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
}
```

### Friend Relationship Entity
```java
@Entity
@Table(name = "friendships")
public class Friendship {
    @Id
    private String friendshipId;
    
    private String userId;
    private String friendId;
    
    private FriendshipStatus status; // PENDING, ACCEPTED, BLOCKED
    private LocalDateTime requestedAt;
    private LocalDateTime acceptedAt;
    
    // Custom relationship metadata
    private String nickname;
    private List<String> circles; // friend, family, colleague
    private PrivacyLevel privacyLevel;
}
```

## 🔧 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySql 8+
- Redis 6.x
- SMTP server (for email verification)
- SMS gateway (for phone verification)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/JLSS-virtual/PlaceLive-User-Service.git
   cd PlaceLive-User-Service
   ```

2. **Configure database**
   ```yaml
   # application.yml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/placelive_users
       username: your_username
       password: your_password
   ```

3. **Configure JWT settings**
   ```yaml
   jwt:
     secret: your-secret-key
     expiration: 86400000 # 24 hours
     refresh-expiration: 604800000 # 7 days
   ```

4. **Configure notification services**
   ```yaml
   notification:
     email:
       smtp-host: smtp.gmail.com
       smtp-port: 587
       username: your-email@gmail.com
       password: your-app-password
     sms:
       provider: twilio
       account-sid: your-account-sid
       auth-token: your-auth-token
   ```

5. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

### Docker Deployment
```bash
docker build -t placelive-user-service .
docker run -p 8081:8081 placelive-user-service
```

## 🌐 API Documentation

### Authentication Endpoints

#### User Registration
```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "phoneNumber": "+1234567890",
  "username": "johndoe",
  "password": "SecurePassword123!",
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1995-06-15"
}
```

#### User Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePassword123!"
}
```

#### Token Refresh
```http
POST /api/v1/auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### User Management Endpoints

#### Get User Profile
```http
GET /api/v1/users/profile
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### Update User Profile
```http
PUT /api/v1/users/profile
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Smith",
  "profilePictureUrl": "https://example.com/profile.jpg"
}
```

#### Search Users
```http
GET /api/v1/users/search?query=john&limit=10
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Friend Management Endpoints

#### Send Friend Request
```http
POST /api/v1/friends/request
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
  "targetUserId": "user456",
  "message": "Hi! Let's connect on PlaceLive"
}
```

#### Accept Friend Request
```http
POST /api/v1/friends/accept/{requestId}
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### Get Friends List
```http
GET /api/v1/friends?page=0&size=20
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### Get Followers
```http
GET /api/v1/users/{userId}/followers
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Privacy Management Endpoints

#### Update Privacy Settings
```http
PUT /api/v1/users/privacy
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
  "defaultPrivacyLevel": "FRIENDS_ONLY",
  "locationSharingEnabled": true,
  "friendRequestsEnabled": true,
  "placeSpecificSettings": {
    "home": "PRIVATE",
    "work": "COLLEAGUES_ONLY",
    "public": "FRIENDS_ONLY"
  }
}
```

## 🔒 Security Features

### Authentication & Authorization
```java
@Service
public class AuthenticationService {
    
    public AuthResponse authenticate(LoginRequest request) {
        // Validate credentials
        // Generate JWT tokens
        // Update last login timestamp
        // Return tokens and user info
    }
    
    public AuthResponse refreshToken(String refreshToken) {
        // Validate refresh token
        // Generate new access token
        // Optionally rotate refresh token
    }
}
```

### Password Security
```java
@Service
public class PasswordService {
    
    private final BCryptPasswordEncoder passwordEncoder;
    
    public String hashPassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }
    
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return passwordEncoder.matches(plainPassword, hashedPassword);
    }
    
    public void validatePasswordStrength(String password) {
        // Check length, complexity, common patterns
        // Throw exception if weak
    }
}
```

### Multi-Factor Authentication
```java
@Service
public class MFAService {
    
    public void sendEmailVerification(String email) {
        String token = generateVerificationToken();
        emailService.sendVerificationEmail(email, token);
        cacheService.storeToken(email, token, Duration.ofMinutes(15));
    }
    
    public void sendSMSVerification(String phoneNumber) {
        String otp = generateOTP();
        smsService.sendOTP(phoneNumber, otp);
        cacheService.storeOTP(phoneNumber, otp, Duration.ofMinutes(5));
    }
}
```

## 👥 Social Features Implementation

### Friend Request System
```java
@Service
public class FriendshipService {
    
    public void sendFriendRequest(String senderId, String receiverId, String message) {
        // Validate users exist and are not already friends
        // Check privacy settings
        // Create friendship record with PENDING status
        // Send notification to receiver
    }
    
    public void acceptFriendRequest(String requestId, String userId) {
        // Validate request exists and user is receiver
        // Update status to ACCEPTED
        // Create reciprocal friendship record
        // Send notification to sender
    }
    
    public List<User> getFriends(String userId, Pageable pageable) {
        // Query accepted friendships
        // Apply privacy filters
        // Return paginated friend list
    }
}
```

### Social Graph Analytics
```java
@Service
public class SocialGraphService {
    
    public List<User> getMutualFriends(String userId1, String userId2) {
        // Find common friends between two users
        // Apply privacy controls
        // Return filtered list
    }
    
    public List<User> getSuggestedFriends(String userId) {
        // Analyze social graph patterns
        // Consider mutual friends, location proximity
        // Apply machine learning recommendations
        // Return suggested connections
    }
    
    public SocialMetrics calculateSocialMetrics(String userId) {
        // Calculate network size, engagement levels
        // Identify influential connections
        // Generate social insights
    }
}
```

## 🔐 Privacy Controls

### Granular Privacy Settings
```java
@Entity
public class PrivacySettings {
    private String userId;
    
    // General privacy levels
    private PrivacyLevel profileVisibility;
    private PrivacyLevel friendListVisibility;
    private PrivacyLevel locationSharingLevel;
    
    // Place-specific settings
    @ElementCollection
    private Map<PlaceType, PrivacyLevel> placeSpecificSettings;
    
    // Relationship-specific settings
    @ElementCollection
    private Map<String, PrivacyLevel> friendSpecificSettings;
    
    // Communication preferences
    private boolean allowFriendRequests;
    private boolean allowLocationBasedNotifications;
    private boolean allowSponsoredMessages;
}
```

### Role-Based Access Control
```java
@Service
public class PrivacyControlService {
    
    public boolean canUserViewProfile(String viewerId, String targetUserId) {
        // Check relationship status
        // Apply privacy settings
        // Consider role-based permissions
        return hasPermission(viewerId, targetUserId, Permission.VIEW_PROFILE);
    }
    
    public boolean canUserSeeLocation(String viewerId, String targetUserId, String placeId) {
        // Get place type and user privacy settings
        // Check friend relationship and circles
        // Apply place-specific rules
        return hasLocationPermission(viewerId, targetUserId, placeId);
    }
}
```

## 📊 User Analytics & Insights

### User Behavior Analytics
```java
@Service
public class UserAnalyticsService {
    
    public UserBehaviorProfile generateBehaviorProfile(String userId) {
        // Analyze login patterns, app usage
        // Social interaction frequency
        // Location sharing preferences
        // Generate insights for personalization
    }
    
    public List<PlaceRecommendation> generatePlaceRecommendations(String userId) {
        // Analyze user's place history
        // Consider friend preferences
        // Apply collaborative filtering
        // Return personalized recommendations
    }
}
```

### Social Network Analysis
```java
@Service
public class NetworkAnalysisService {
    
    public CommunityStructure detectCommunities(String userId) {
        // Apply community detection algorithms
        // Identify natural friend groups
        // Suggest circle organizations
    }
    
    public InfluenceMetrics calculateInfluence(String userId) {
        // Measure user's network influence
        // Calculate centrality metrics
        // Identify key connectors
    }
}
```

## 🚀 Advanced Features

### Smart Friend Suggestions
- **Location-Based Matching**: Suggest friends who visit similar places
- **Interest Correlation**: Match users with similar place preferences
- **Mutual Friend Analysis**: Leverage existing connections for suggestions
- **Behavioral Similarity**: Find users with similar app usage patterns

### Dynamic Privacy Adaptation
- **Context-Aware Privacy**: Automatically adjust settings based on location
- **Time-Based Controls**: Different privacy levels for different times
- **Event-Driven Adjustments**: Modify visibility during special events
- **Machine Learning Privacy**: Learn user preferences and suggest optimal settings

### Social Health Monitoring
- **Relationship Quality Metrics**: Measure friendship strength and engagement
- **Social Isolation Detection**: Identify users who might need social support
- **Unhealthy Pattern Recognition**: Detect potential cyberbullying or harassment
- **Wellness Recommendations**: Suggest activities to improve social connections

## 📈 Performance & Scalability

### Database Optimization
```sql
-- Optimized indexes for common queries
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_phone ON users(phone_number);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_friendships_user_status ON friendships(user_id, status);
CREATE INDEX idx_friendships_friend_status ON friendships(friend_id, status);

-- Composite indexes for complex queries
CREATE INDEX idx_users_search ON users(username, first_name, last_name);
CREATE INDEX idx_friendships_mutual ON friendships(user_id, friend_id, status);
```

### Caching Strategy
```java
@Service
public class UserCacheService {
    
    @Cacheable(value = "users", key = "#userId")
    public User getUserById(String userId) {
        return userRepository.findById(userId);
    }
    
    @Cacheable(value = "friendships", key = "#userId")
    public List<String> getFriendIds(String userId) {
        return friendshipRepository.findAcceptedFriendIds(userId);
    }
    
    @CacheEvict(value = "users", key = "#userId")
    public void updateUser(String userId, User user) {
        userRepository.save(user);
    }
}
```

## 🔄 Integration Patterns

### Event Publishing
```java
@Component
public class UserEventPublisher {
    
    @EventListener
    public void publishUserRegistrationEvent(UserRegisteredEvent event) {
        // Notify other services about new user
        kafkaTemplate.send("user-events", event);
    }
    
    @EventListener
    public void publishFriendshipEvent(FriendshipChangedEvent event) {
        // Update social graph in other services
        kafkaTemplate.send("friendship-events", event);
    }
}
```

### Service Communication
```java
@Component
public class UserServiceClient {
    
    public List<User> getUsersByIds(List<String> userIds) {
        // Batch fetch users for other services
        return userRepository.findAllById(userIds);
    }
    
    public boolean areUsersFriends(String userId1, String userId2) {
        // Quick friendship check for other services
        return friendshipRepository.existsByUserIdAndFriendIdAndStatus(
            userId1, userId2, FriendshipStatus.ACCEPTED);
    }
}
```

## 🛠️ Development

### Code Structure
```
src/
├── main/java/com/placelive/user/
│   ├── controller/          # REST API endpoints
│   │   ├── AuthController.java
│   │   ├── UserController.java
│   │   └── FriendController.java
│   ├── service/            # Business logic layer
│   │   ├── AuthenticationService.java
│   │   ├── UserService.java
│   │   ├── FriendshipService.java
│   │   └── PrivacyService.java
│   ├── repository/         # Data access layer
│   ├── model/              # Entity definitions
│   ├── dto/                # Data transfer objects
│   ├── security/           # Security configuration
│   ├── config/             # Configuration classes
│   └── util/               # Utility classes
├── main/resources/
│   ├── application.yml     # Configuration
│   └── db/migration/       # Database scripts
└── test/                   # Unit and integration tests
```

### Testing Strategy
```java
@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {
    
    @Test
    void shouldFindUserByEmail() {
        // Test user lookup by email
    }
    
    @Test
    void shouldCreateFriendshipSuccessfully() {
        // Test friendship creation
    }
}

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Test
    void shouldReturnUserProfile() {
        // Test profile retrieval API
    }
    
    @Test
    void shouldHandleInvalidAuthentication() {
        // Test security scenarios
    }
}
```

## 🤝 Contributing

We welcome contributions to PlaceLive-User-Service! Here's how to get started:

### Development Guidelines
1. **Code Style**: Follow Spring Boot conventions and Google Java Style Guide
2. **Testing**: Maintain 80%+ test coverage with unit and integration tests
3. **Security**: All authentication and authorization changes require security review
4. **Privacy**: Any privacy-related changes must be reviewed for GDPR compliance
5. **Documentation**: Update API documentation for any endpoint changes

### Pull Request Process
1. Fork the repository
2. Create a feature branch: `git checkout -b feature/enhanced-privacy-controls`
3. Write comprehensive tests
4. Update documentation
5. Submit pull request with detailed description

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- **Documentation**: [Wiki](https://github.com/JLSS-virtual/PlaceLive-User-Service/wiki)
- **Issues**: [GitHub Issues](https://github.com/JLSS-virtual/PlaceLive-User-Service/issues)
- **Security Issues**: security@placelive.com
- **General Support**: jlss.virtual.0808@gmail.com

---

**PlaceLive-User-Service**: Building the social foundation for location-aware experiences with privacy, security, and user empowerment at the core. 👥🔐
