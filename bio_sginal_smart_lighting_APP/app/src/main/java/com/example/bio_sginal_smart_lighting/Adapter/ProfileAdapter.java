package com.example.bio_sginal_smart_lighting.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bio_sginal_smart_lighting.AddProfileActivity;
import com.example.bio_sginal_smart_lighting.DetailProfileActivity;
import com.example.bio_sginal_smart_lighting.R;
import com.example.bio_sginal_smart_lighting.SocketIO.Profile;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // 뷰 타입을 구분하기 위한 상수
    private static final int VIEW_TYPE_PROFILE = 0;
    private static final int VIEW_TYPE_ADD_PROFILE = 1;

    // 프로필 리스트를 저장하는 변수
    private List<Profile> profiles;

    // 생성자 - 프로필 리스트를 어댑터에 전달
    public ProfileAdapter(List<Profile> profiles) {
        this.profiles = profiles;
    }

    // 프로필 데이터를 갱신하는 메서드
    public void updateProfiles(List<Profile> newProfiles) {
        this.profiles = newProfiles;
        notifyDataSetChanged();  // 전체 데이터를 다시 그리기
    }

    @Override
    public int getItemViewType(int position) {
        // 마지막 항목에 '프로필 추가' 버튼을 넣기 위한 로직
        if (position == profiles.size()) {
            return VIEW_TYPE_ADD_PROFILE; // 프로필 추가 버튼을 마지막에 표시
        } else {
            return VIEW_TYPE_PROFILE; // 일반 프로필 항목
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 프로필 뷰 타입에 따라 다른 레이아웃을 인플레이트하여 ViewHolder를 생성
        if (viewType == VIEW_TYPE_PROFILE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
            return new ProfileViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_button, parent, false);
            return new AddProfileViewHolder(view, this);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // ViewHolder 타입에 따라 데이터를 바인딩
        if (holder instanceof ProfileViewHolder) {
            Profile profile = profiles.get(position);
            ((ProfileViewHolder) holder).bind(profile);
        } else if (holder instanceof AddProfileViewHolder) {
            ((AddProfileViewHolder) holder).bind();
        }
    }

    @Override
    public int getItemCount() {
        return profiles.size() + 1; // 프로필 목록 크기에 +1 하여 마지막에 추가 버튼 포함
    }


    // 일반 프로필을 표시하는 ViewHolder 클래스
    class ProfileViewHolder extends RecyclerView.ViewHolder {
        private ImageView profileImage;
        private TextView profileName;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            // 레이아웃에서 프로필 이미지와 이름을 참조
            profileImage = itemView.findViewById(R.id.profile_image);
            profileName = itemView.findViewById(R.id.profile_name);
        }

        // 프로필 데이터를 UI에 바인딩
        public void bind(Profile profile) {

            profileImage.setImageURI(Uri.parse(profile.getImageUri()));
            profileName.setText(profile.getName());

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), DetailProfileActivity.class);

                intent.putExtra("profile_image", profile.getImageUri());
                intent.putExtra("profile_name", profile.getName());
                v.getContext().startActivity(intent);
            });

        }
    }

    // '프로필 추가' 버튼을 표시하는 ViewHolder 클래스
    class AddProfileViewHolder extends RecyclerView.ViewHolder {
        private Button addProfileButton;
        private ProfileAdapter adapter;

        public AddProfileViewHolder(View itemView, ProfileAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            // 레이아웃에서 '프로필 추가' 버튼을 참조
            addProfileButton = itemView.findViewById(R.id.add_profile_button);
        }

        // '프로필 추가' 버튼의 동작 정의
        public void bind() {
            addProfileButton.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), AddProfileActivity.class);
                ((AppCompatActivity) itemView.getContext()).startActivityForResult(intent, 1);
            });

        }
    }
}

