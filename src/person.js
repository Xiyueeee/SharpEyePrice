import { createStore } from 'vuex';

const person = createStore({
  state: {
    user: {
      account: '',
      email: ''
    }
  },
  mutations: {
    setUser(state, user) {
      state.user = user;
    }
  },
  actions: {
    setUser({ commit }, user) {
      commit('setUser', user);
    }
  },
  getters: {
    user(state) {
      return state.user;
    }
  }
});

export default person;