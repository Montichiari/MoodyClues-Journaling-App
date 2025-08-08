package com.moodyclues.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moodyclues.model.CounsellorUser;
import com.moodyclues.model.JournalUser;
import com.moodyclues.model.User;
import com.moodyclues.repository.CounsellorRepository;
import com.moodyclues.repository.JournalUserRepository;
import com.moodyclues.service.CounsellorService;
import com.moodyclues.service.JournalUserService;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	JournalUserRepository juserRepo;
	
	@Autowired
	CounsellorRepository cRepo;
	
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Try JournalUser first
        Optional<JournalUser> journalUser = juserRepo.findJournalUserByEmail(email);
        if (journalUser.isPresent()) {
            return buildUserDetails(journalUser.get(), "ROLE_JOURNAL");
        }

        // Then try CounsellorUser
        Optional<CounsellorUser> counsellorUser = cRepo.findCounsellorByEmail(email);
        if (counsellorUser.isPresent()) {
            return buildUserDetails(counsellorUser.get(), "ROLE_COUNSELLOR");
        }

        throw new UsernameNotFoundException("User not found");
    }

    private UserDetails buildUserDetails(User user, String role) {
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority(role))
        );
    }

}
