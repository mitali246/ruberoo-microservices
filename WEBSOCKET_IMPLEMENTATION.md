# WebSocket Implementation Documentation
# Real-Time GPS Tracking for Ruberoo Ride-Sharing Platform

## Overview
This document details the WebSocket implementation for real-time GPS tracking in the Ruberoo Tracking Service. The implementation enables bidirectional, low-latency communication between drivers and riders for live location updates.

## Architecture

### Technology Stack
- **Protocol**: STOMP (Simple Text Oriented Messaging Protocol) over WebSocket
- **Framework**: Spring Boot 3.2.2 with Spring WebSocket & WebFlux
- **Message Broker**: Simple In-Memory Broker (production should use RabbitMQ/Redis)
- **Fallback**: SockJS for browsers without WebSocket support
- **Port**: 8084 (Tracking Service)

### Components Implemented

#### 1. WebSocket Configuration
**File**: `ruberoo-tracking-service/src/main/java/com/ruberoo/tracking_service/config/WebSocketConfig.java`

**Features**:
- STOMP endpoint: `/ws/tracking`
- Message broker prefix: `/topic`
- Application destination prefix: `/app`
- SockJS fallback enabled
- CORS configured (wildcard for development)

**Configuration Details**:
```
WebSocket Handshake: ws://localhost:8084/ws/tracking
Message Broker: /topic/* (pub/sub destinations)
Application Routes: /app/* (client-to-server messages)
```

#### 2. Location Update DTO
**File**: `ruberoo-tracking-service/src/main/java/com/ruberoo/tracking_service/dto/LocationUpdateDto.java`

**Properties**:
- `rideId` (Long) - Unique ride identifier
- `driverId` (Long) - Driver identifier
- `latitude` (Double) - GPS latitude (-90 to 90)
- `longitude` (Double) - GPS longitude (-180 to 180)
- `speed` (Double) - Speed in km/h
- `heading` (Double) - Direction in degrees (0-360)
- `timestamp` (LocalDateTime) - Update timestamp
- `status` (String) - Movement status (MOVING/STOPPED/IDLE)

#### 3. WebSocket Controller
**File**: `ruberoo-tracking-service/src/main/java/com/ruberoo/tracking_service/controller/TrackingWebSocketController.java`

**WebSocket Endpoints**:

| Endpoint | Type | Description |
|----------|------|-------------|
| `/app/tracking/update/{rideId}` | Send | Driver sends location update |
| `/topic/tracking/{rideId}` | Subscribe | Rider receives location updates |

**REST Endpoints**:

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/tracking/health` | GET | Health check |
| `/api/tracking/rides/{rideId}/location` | GET | Get last known location |

**Features**:
- Real-time location broadcasting
- Input validation (lat/lon/speed/heading ranges)
- Logging for all location updates
- Programmatic message sending capability
- TODO markers for database persistence and ETA calculation

## Usage Guide

### 1. Driver (Sending Location Updates)

#### JavaScript Client (SockJS + STOMP)
```javascript
// Connect to WebSocket
const socket = new SockJS('http://localhost:8084/ws/tracking');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Connected to WebSocket');
    
    // Send location update
    const locationUpdate = {
        rideId: 1001,
        driverId: 2001,
        latitude: 37.7749,
        longitude: -122.4194,
        speed: 45.5,
        heading: 180,
        status: 'MOVING'
    };
    
    stompClient.send(
        '/app/tracking/update/1001',
        {},
        JSON.stringify(locationUpdate)
    );
});
```

#### Spring Boot Backend
```java
@Autowired
private TrackingWebSocketController trackingController;

public void sendDriverLocation(Long rideId, LocationUpdateDto location) {
    trackingController.sendLocationUpdate(rideId, location);
}
```

### 2. Rider (Receiving Location Updates)

#### JavaScript Client (SockJS + STOMP)
```javascript
// Connect and subscribe
const socket = new SockJS('http://localhost:8084/ws/tracking');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    // Subscribe to ride location updates
    stompClient.subscribe('/topic/tracking/1001', function(message) {
        const location = JSON.parse(message.body);
        console.log('Driver location:', location);
        updateMapMarker(location.latitude, location.longitude);
    });
});
```

### 3. Testing with HTML Client

A complete test client is provided: `websocket-test-client.html`

**To Use**:
1. Ensure Tracking Service is running on port 8084
2. Open `websocket-test-client.html` in a browser
3. Enter Ride ID and click "Connect"
4. Click "Send Location" to simulate GPS updates
5. Watch real-time updates in the log and location display

**Features**:
- ✅ Connection management
- ✅ Real-time location streaming
- ✅ Visual feedback and logging
- ✅ Coordinate simulation (auto-increment)
- ✅ Beautiful modern UI

## Message Flow

### Complete Flow Diagram
```
┌─────────────┐                    ┌─────────────────────┐                    ┌─────────────┐
│   Driver    │                    │  Tracking Service   │                    │    Rider    │
│   Mobile    │                    │   (Port 8084)       │                    │   Mobile    │
└──────┬──────┘                    └──────────┬──────────┘                    └──────┬──────┘
       │                                      │                                       │
       │  1. Connect to WebSocket             │                                       │
       │─────────────────────────────────────>│                                       │
       │                                      │                                       │
       │  2. WebSocket Established            │                                       │
       │<─────────────────────────────────────│                                       │
       │                                      │                                       │
       │                                      │  3. Subscribe to /topic/tracking/1001 │
       │                                      │<──────────────────────────────────────│
       │                                      │                                       │
       │  4. Send GPS Update                  │                                       │
       │  /app/tracking/update/1001           │                                       │
       │─────────────────────────────────────>│                                       │
       │                                      │                                       │
       │                                      │  5. Validate & Process                │
       │                                      │  - Check lat/lon ranges               │
       │                                      │  - Add timestamp                      │
       │                                      │  - Log update                         │
       │                                      │                                       │
       │                                      │  6. Broadcast to Subscribers          │
       │                                      │  /topic/tracking/1001                 │
       │                                      │──────────────────────────────────────>│
       │                                      │                                       │
       │                                      │                                       │  7. Update Map UI
       │                                      │                                       │  - Move marker
       │                                      │                                       │  - Update ETA
       │                                      │                                       │
       │  8. Continuous updates every 3-5s    │                                       │
       │─────────────────────────────────────>│──────────────────────────────────────>│
```

## Integration with Existing Services

### 1. API Gateway Integration
**File**: `ruberoo-api-gateway/src/main/resources/application.yml`

Add route for WebSocket traffic:
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: tracking-ws
          uri: ws://tracking-service:8084
          predicates:
            - Path=/ws/tracking/**
          filters:
            - RewritePath=/ws/(?<segment>.*), /$\{segment}
```

### 2. Ride Management Service Integration
When a ride is accepted, notify the tracking service:
```java
@Autowired
private RestTemplate restTemplate;

public void onRideAccepted(Long rideId, Long driverId) {
    // Initialize tracking session
    LocationUpdateDto initialLocation = new LocationUpdateDto();
    initialLocation.setRideId(rideId);
    initialLocation.setDriverId(driverId);
    
    restTemplate.postForEntity(
        "http://tracking-service:8084/api/tracking/initialize",
        initialLocation,
        Void.class
    );
}
```

### 3. Frontend Integration (React/Angular)
Install SockJS and STOMP:
```bash
npm install sockjs-client stompjs
```

React component:
```jsx
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

const TrackingMap = ({ rideId }) => {
    useEffect(() => {
        const socket = new SockJS('http://localhost:9095/ws/tracking');
        const client = Stomp.over(socket);
        
        client.connect({}, () => {
            client.subscribe(`/topic/tracking/${rideId}`, (message) => {
                const location = JSON.parse(message.body);
                updateMap(location);
            });
        });
        
        return () => client.disconnect();
    }, [rideId]);
};
```

## Validation Rules

### Location Data Validation
The controller validates all incoming location updates:

| Field | Validation Rule | Error Handling |
|-------|----------------|----------------|
| `latitude` | -90 to 90 | Reject, log warning |
| `longitude` | -180 to 180 | Reject, log warning |
| `speed` | 0 to 200 km/h | Reject, log warning |
| `heading` | 0 to 360 degrees | Reject, log warning |
| `rideId` | Not null | Return null |

## Performance Considerations

### Current Implementation (Development)
- **Message Broker**: Simple in-memory broker
- **Scalability**: Limited to single instance
- **Message Rate**: ~1 message per 3-5 seconds per ride
- **Concurrent Rides**: Up to 100 rides (estimated)

### Production Recommendations
1. **External Message Broker**:
   - Use RabbitMQ or Redis Pub/Sub
   - Enables horizontal scaling
   - Persists messages across restarts

2. **Load Balancing**:
   - Use sticky sessions or session affinity
   - WebSocket connections are stateful

3. **Caching**:
   - Cache last known locations in Redis
   - Reduce database load
   - Enable quick location retrieval

4. **Rate Limiting**:
   - Limit GPS updates to 1 per 3 seconds per driver
   - Prevent message flooding

## Security Considerations

### Current State (⚠️ Development Only)
- ❌ CORS set to wildcard (`*`)
- ❌ No authentication on WebSocket handshake
- ❌ No message-level authorization
- ❌ No encryption (ws:// instead of wss://)

### Production Requirements
1. **Authentication**:
   ```java
   @Override
   public void configureClientInboundChannel(ChannelRegistration registration) {
       registration.interceptors(new AuthChannelInterceptor());
   }
   ```

2. **Authorization**:
   - Verify driver can only update their own location
   - Verify rider can only subscribe to their own rides

3. **TLS/SSL**:
   - Use `wss://` instead of `ws://`
   - Configure SSL certificates

4. **CORS**:
   ```java
   registry.addEndpoint("/ws/tracking")
           .setAllowedOrigins("https://ruberoo.com")
           .withSockJS();
   ```

## Testing

### Unit Tests (TODO)
```java
@SpringBootTest
@AutoConfigureMockMvc
class TrackingWebSocketControllerTest {
    
    @Test
    void testLocationValidation() {
        LocationUpdateDto invalidLocation = new LocationUpdateDto();
        invalidLocation.setLatitude(100.0); // Invalid
        
        boolean isValid = controller.isValidLocation(invalidLocation);
        assertFalse(isValid);
    }
}
```

### Integration Tests (TODO)
```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class WebSocketIntegrationTest {
    
    @Test
    void testLocationBroadcast() {
        // Connect WebSocket client
        // Send location update
        // Verify subscribers receive update
    }
}
```

### Manual Testing Checklist
- [x] WebSocket connection established
- [x] Location updates sent successfully
- [x] Location updates received by subscribers
- [x] Invalid data rejected (lat/lon out of range)
- [ ] Multiple concurrent subscribers
- [ ] Connection recovery after disconnect
- [ ] Load testing (100+ simultaneous rides)

## Monitoring & Logging

### Log Patterns
All location updates are logged:
```
INFO: Received location update for ride 1001: lat=37.7749, lon=-122.4194, speed=45.5
WARN: Invalid location data received for ride 1001
```

### Metrics to Track (TODO)
- WebSocket connections (active/total)
- Messages per second
- Average message latency
- Connection failures
- Invalid message rate

### Prometheus Integration (TODO)
```java
@Counted(value = "tracking.location.updates")
@Timed(value = "tracking.location.latency")
public LocationUpdateDto handleLocationUpdate(...) {
    // ...
}
```

## Deployment

### Docker Configuration
The tracking service is already containerized. No changes needed:
```yaml
# docker-compose.yml
tracking-service:
  image: ruberoo-tracking-service:latest
  ports:
    - "8084:8084"
  environment:
    - SPRING_PROFILES_ACTIVE=docker
```

### Kubernetes Configuration
For production Kubernetes deployment:
```yaml
apiVersion: v1
kind: Service
metadata:
  name: tracking-service
spec:
  type: LoadBalancer
  selector:
    app: tracking-service
  ports:
    - name: http
      port: 8084
      targetPort: 8084
  sessionAffinity: ClientIP  # Important for WebSocket
```

## Future Enhancements

### Planned Features
1. **Historical Tracking**:
   - Persist all location updates to database
   - Provide ride replay functionality
   - Generate heatmaps of popular routes

2. **ETA Calculation**:
   - Calculate distance remaining
   - Estimate arrival time using traffic data
   - Send ETA updates to riders

3. **Route Deviation Detection**:
   - Compare actual route vs. expected route
   - Alert riders of significant deviations
   - Provide safety features

4. **Geofencing**:
   - Trigger events when entering/leaving areas
   - Notify on airport/station arrivals
   - Surge pricing zone detection

5. **Analytics**:
   - Average speed per driver
   - Route efficiency analysis
   - Driver behavior scoring

## API Reference

### WebSocket Endpoints

#### Send Location Update
**Destination**: `/app/tracking/update/{rideId}`
**Method**: SEND
**Payload**:
```json
{
  "rideId": 1001,
  "driverId": 2001,
  "latitude": 37.7749,
  "longitude": -122.4194,
  "speed": 45.5,
  "heading": 180,
  "status": "MOVING"
}
```

#### Subscribe to Location Updates
**Destination**: `/topic/tracking/{rideId}`
**Method**: SUBSCRIBE
**Response**:
```json
{
  "rideId": 1001,
  "driverId": 2001,
  "latitude": 37.7749,
  "longitude": -122.4194,
  "speed": 45.5,
  "heading": 180,
  "status": "MOVING",
  "timestamp": "2025-01-24T10:30:45"
}
```

### REST Endpoints

#### Health Check
```
GET /api/tracking/health
Response: 200 OK "Tracking WebSocket service is running"
```

#### Get Last Known Location
```
GET /api/tracking/rides/{rideId}/location
Response: 200 OK (LocationUpdateDto) or 404 Not Found
```

## Troubleshooting

### Common Issues

#### 1. Connection Failed
**Symptom**: WebSocket connection refused
**Solutions**:
- Verify Tracking Service is running: `docker ps | grep tracking`
- Check port 8084 is accessible: `curl http://localhost:8084/api/tracking/health`
- Review logs: `docker logs ruberoo-tracking-service`

#### 2. Messages Not Received
**Symptom**: Location updates sent but not received
**Solutions**:
- Verify subscription topic matches send destination
- Check browser console for JavaScript errors
- Verify rideId is consistent between send/subscribe

#### 3. Invalid Location Rejected
**Symptom**: Location updates rejected by server
**Solutions**:
- Check latitude is between -90 and 90
- Check longitude is between -180 and 180
- Check speed is between 0 and 200 km/h
- Check heading is between 0 and 360 degrees

## Dependencies Added

### Maven Dependencies
```xml
<!-- WebSocket for real-time GPS tracking -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

## Files Created/Modified

### New Files
1. ✅ `ruberoo-tracking-service/src/main/java/com/ruberoo/tracking_service/config/WebSocketConfig.java`
2. ✅ `ruberoo-tracking-service/src/main/java/com/ruberoo/tracking_service/dto/LocationUpdateDto.java`
3. ✅ `ruberoo-tracking-service/src/main/java/com/ruberoo/tracking_service/controller/TrackingWebSocketController.java`
4. ✅ `ruberoo-tracking-service/websocket-test-client.html`
5. ✅ `WEBSOCKET_IMPLEMENTATION.md` (this file)

### Modified Files
1. ✅ `ruberoo-tracking-service/pom.xml` (added WebSocket dependencies)

## Build & Run

### Rebuild Service
```bash
cd ruberoo-tracking-service
mvn clean install -DskipTests
docker-compose build tracking-service
docker-compose up -d tracking-service
```

### Verify Deployment
```bash
# Check container is running
docker ps | grep tracking

# Check logs
docker logs -f ruberoo-tracking-service

# Test health endpoint
curl http://localhost:8084/api/tracking/health
```

### Test WebSocket
1. Open `websocket-test-client.html` in browser
2. Enter Ride ID: 1001
3. Click "Connect"
4. Click "Send Location"
5. Verify location appears in display panel

## Support & Documentation

### Related Documentation
- [TECHNICAL_ANALYSIS.md](./TECHNICAL_ANALYSIS.md) - Complete system analysis
- [SECURITY_VULNERABILITY_ANALYSIS.md](./SECURITY_VULNERABILITY_ANALYSIS.md) - Security audit
- [ARCHITECTURE_DIAGRAMS.md](./ARCHITECTURE_DIAGRAMS.md) - Architecture diagrams

### References
- Spring WebSocket Documentation: https://spring.io/guides/gs/messaging-stomp-websocket/
- STOMP Protocol: https://stomp.github.io/
- SockJS: https://github.com/sockjs/sockjs-client

---

**Document Version**: 1.0  
**Last Updated**: 2025-01-24  
**Authors**: Ruberoo Development Team  
**Status**: ✅ Implementation Complete - Ready for Testing
