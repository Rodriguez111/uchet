package uchet.service;

import uchet.models.Path;

import java.util.List;
import java.util.Map;

public interface AuthorityService {
List<Path> getAll();
    Map<String, String> addAuthority(String authorityName);
}
