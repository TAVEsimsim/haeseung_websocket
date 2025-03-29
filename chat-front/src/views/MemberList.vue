<template>
    <v-container>
        <v-row>
            <v-col>
                <v-card>
                    <v-card-title class="text-center text-h5">
                        회원목록
                    </v-card-title>
                    <v-card-text>
                        <v-table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>이름</th>
                                    <th>email</th>
                                    <th>채팅</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="member in memberList" :key="member.id">
                                    <td>{{member.id}}</td>
                                    <td>{{member.name}}</td>
                                    <td>{{member.email}}</td>
                                    <td>
                                        <v-btn color="primary" @click="startChat(member.id)">채팅하기</v-btn>
                                    </td>
                                </tr>
                            </tbody>
                        </v-table>
                    </v-card-text>
                </v-card>

            </v-col>

        </v-row>

    </v-container>
</template>

<script>
import axios from 'axios';
export default{
    data(){
        return {
            memberList: []
        }
    },
    async created(){
        const response = await axios.get(`http://localhost:8080/member/list`)
        this.memberList = response.data;
    }
}
</script>