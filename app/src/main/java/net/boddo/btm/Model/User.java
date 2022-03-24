package net.boddo.btm.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class User implements Parcelable {

        @SerializedName("status")
        @Expose
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public static Creator<User> getCREATOR() {
            return CREATOR;
        }

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("profile_photo")
        @Expose
        private String profilePhoto;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("relationship")
        @Expose
        private String relationship;
        @SerializedName("date_of_birth")
        @Expose
        private String dateOfBirth;
        @SerializedName("hometown")
        @Expose
        private String hometown;
        @SerializedName("about")
        @Expose
        private String about;
        @SerializedName("education")
        @Expose
        private String education;
        @SerializedName("language")
        @Expose
        private String language;
        @SerializedName("looking_for")
        @Expose
        private String lookingFor;
        @SerializedName("interest_in")
        @Expose
        private String interestIn;
        @SerializedName("personality")
        @Expose
        private String personality;
        @SerializedName("height")
        @Expose
        private String height;
        @SerializedName("eye_color")
        @Expose
        private String eyeColor;
        @SerializedName("hair_color")
        @Expose
        private String hairColor;
        @SerializedName("habits")
        @Expose
        private String habits;
        @SerializedName("smoking")
        @Expose
        private String smoking;
        @SerializedName("profession")
        @Expose
        private String profession;
        @SerializedName("moto")
        @Expose
        private String moto;
        @SerializedName("is_email_verified")
        @Expose
        private String isEmailVerified;
        @SerializedName("account_created")
        @Expose
        private String accountCreated;
        @SerializedName("is_active")
        @Expose
        private String isActive;
        @SerializedName("is_suspended")
        @Expose
        private String isSuspended;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("is_pulup_plus_subcriber")
        @Expose
        private String isPulupPlusSubcriber;
        @SerializedName("deleted_at")
        @Expose
        private String deletedAt;
        @SerializedName("android_id")
        @Expose
        private String androidId;
        @SerializedName("reset_token")
        @Expose
        private String resetToken;
        @SerializedName("current_location")
        @Expose
        private String currentLocation;
        @SerializedName("palup_point")
        @Expose
        private String palupPoint;

        protected User(Parcel in) {
            userId = in.readString();
            userName = in.readString();
            email = in.readString();
            firstName = in.readString();
            profilePhoto = in.readString();
            password = in.readString();
            gender = in.readString();
            dateOfBirth = in.readString();
            language = in.readString();
            moto = in.readString();
            isEmailVerified = in.readString();
            accountCreated = in.readString();
            isActive = in.readString();
            isSuspended = in.readString();
            updatedAt = in.readString();
            isPulupPlusSubcriber = in.readString();
            androidId = in.readString();
            currentLocation = in.readString();
            palupPoint = in.readString();
        }

        public static final Creator<User> CREATOR = new Creator<User>() {
            @Override
            public User createFromParcel(Parcel in) {
                return new User(in);
            }

            @Override
            public User[] newArray(int size) {
                return new User[size];
            }
        };

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getProfilePhoto() {
            return profilePhoto;
        }

        public void setProfilePhoto(String profilePhoto) {
            this.profilePhoto = profilePhoto;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getRelationship() {
            return relationship;
        }

        public void setRelationship(String relationship) {
            this.relationship = relationship;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getHometown() {
            return hometown;
        }

        public void setHometown(String hometown) {
            this.hometown = hometown;
        }

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getLookingFor() {
            return lookingFor;
        }

        public void setLookingFor(String lookingFor) {
            this.lookingFor = lookingFor;
        }

        public String getInterestIn() {
            return interestIn;
        }

        public void setInterestIn(String interestIn) {
            this.interestIn = interestIn;
        }

        public String getPersonality() {
            return personality;
        }

        public void setPersonality(String personality) {
            this.personality = personality;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getEyeColor() {
            return eyeColor;
        }

        public void setEyeColor(String eyeColor) {
            this.eyeColor = eyeColor;
        }

        public String getHairColor() {
            return hairColor;
        }

        public void setHairColor(String hairColor) {
            this.hairColor = hairColor;
        }

        public String getHabits() {
            return habits;
        }

        public void setHabits(String habits) {
            this.habits = habits;
        }

        public String getSmoking() {
            return smoking;
        }

        public void setSmoking(String smoking) {
            this.smoking = smoking;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getMoto() {
            return moto;
        }

        public void setMoto(String moto) {
            this.moto = moto;
        }

        public String getIsEmailVerified() {
            return isEmailVerified;
        }

        public void setIsEmailVerified(String isEmailVerified) {
            this.isEmailVerified = isEmailVerified;
        }

        public String getAccountCreated() {
            return accountCreated;
        }

        public void setAccountCreated(String accountCreated) {
            this.accountCreated = accountCreated;
        }

        public String getIsActive() {
            return isActive;
        }

        public void setIsActive(String isActive) {
            this.isActive = isActive;
        }

        public String getIsSuspended() {
            return isSuspended;
        }

        public void setIsSuspended(String isSuspended) {
            this.isSuspended = isSuspended;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getIsPulupPlusSubcriber() {
            return isPulupPlusSubcriber;
        }

        public void setIsPulupPlusSubcriber(String isPulupPlusSubcriber) {
            this.isPulupPlusSubcriber = isPulupPlusSubcriber;
        }

        public String getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(String deletedAt) {
            this.deletedAt = deletedAt;
        }

        public String getAndroidId() {
            return androidId;
        }

        public void setAndroidId(String androidId) {
            this.androidId = androidId;
        }

        public String getResetToken() {
            return resetToken;
        }

        public void setResetToken(String resetToken) {
            this.resetToken = resetToken;
        }

        public String getCurrentLocation() {
            return currentLocation;
        }

        public void setCurrentLocation(String currentLocation) {
            this.currentLocation = currentLocation;
        }

        public String getPalupPoint() {
            return palupPoint;
        }

        public void setPalupPoint(String palupPoint) {
            this.palupPoint = palupPoint;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(userId);
            dest.writeString(userName);
            dest.writeString(email);
            dest.writeString(firstName);
            dest.writeString(profilePhoto);
            dest.writeString(password);
            dest.writeString(gender);
            dest.writeString(dateOfBirth);
            dest.writeString(language);
            dest.writeString(moto);
            dest.writeString(isEmailVerified);
            dest.writeString(accountCreated);
            dest.writeString(isActive);
            dest.writeString(isSuspended);
            dest.writeString(updatedAt);
            dest.writeString(isPulupPlusSubcriber);
            dest.writeString(androidId);
            dest.writeString(currentLocation);
            dest.writeString(palupPoint);
        }
    }

